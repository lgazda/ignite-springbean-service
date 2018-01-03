package pl.net.gazda.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.stream.StreamTransformer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(IgniteClientConfiguration.class);
        ctx.refresh();

        Ignite ignite = ctx.getBean(Ignite.class);
        IgniteAtomicSequence sequence = ignite.atomicSequence("myCustomSequence", atomicConfiguration(), 0, true);
        try(IgniteDataStreamer<Object, Object> dataStreamer = ignite.dataStreamer("testCache")) {
            dataStreamer.receiver(StreamTransformer.from((a,b) -> {
                System.out.println(a.getKey() + " " + a.getValue() + " sequence" + sequence.getAndIncrement());
                return a;
            }));

            for(int i = 0; i < 1000; i++) {
                dataStreamer.addData("key-" + i , "value-" + i);
            }
        }
    }

    public static class CustomTransformer extends StreamTransformer<Object, Object> {
        private final IgniteAtomicSequence sequence;

        public CustomTransformer(IgniteAtomicSequence sequence) {
            this.sequence = sequence;
        }

        @Override
        public Object process(MutableEntry<Object, Object> mutableEntry, Object... objects) throws EntryProcessorException {
            System.out.println(mutableEntry.getKey() + " " + mutableEntry.getValue() + " sequence " + sequence.getAndIncrement());
            return mutableEntry;
        }
    }

    private static AtomicConfiguration atomicConfiguration() {
        AtomicConfiguration atomicConfiguration = new AtomicConfiguration();
        atomicConfiguration.setAtomicSequenceReserveSize(1);
        //this breaks the code
        //atomicConfiguration.setGroupName("PropertyRoomTypeId");
        atomicConfiguration.setCacheMode(CacheMode.PARTITIONED);
        atomicConfiguration.setBackups(2);
        return atomicConfiguration;
    }
}



