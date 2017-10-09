package pl.net.gazda;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteAtomicReference;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteAtomicStamped;
import org.apache.ignite.IgniteBinary;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteCountDownLatch;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.IgniteEvents;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.IgniteLock;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.IgniteScheduler;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.MemoryMetrics;
import org.apache.ignite.PersistenceMetrics;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import org.apache.ignite.lang.IgniteProductVersion;
import org.apache.ignite.plugin.IgnitePlugin;
import org.apache.ignite.plugin.PluginNotFoundException;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.cache.CacheException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

@Primary
@Component
public class FakeIgnite implements Ignite {
    @Override
    public String name() {
        return null;
    }

    @Override
    public IgniteLogger log() {
        return null;
    }

    @Override
    public IgniteConfiguration configuration() {
        return null;
    }

    @Override
    public IgniteCluster cluster() {
        return null;
    }

    @Override
    public IgniteCompute compute() {
        return null;
    }

    @Override
    public IgniteCompute compute(ClusterGroup clusterGroup) {
        return null;
    }

    @Override
    public IgniteMessaging message() {
        return null;
    }

    @Override
    public IgniteMessaging message(ClusterGroup clusterGroup) {
        return null;
    }

    @Override
    public IgniteEvents events() {
        return null;
    }

    @Override
    public IgniteEvents events(ClusterGroup clusterGroup) {
        return null;
    }

    @Override
    public IgniteServices services() {
        return null;
    }

    @Override
    public IgniteServices services(ClusterGroup clusterGroup) {
        return null;
    }

    @Override
    public ExecutorService executorService() {
        return null;
    }

    @Override
    public ExecutorService executorService(ClusterGroup clusterGroup) {
        return null;
    }

    @Override
    public IgniteProductVersion version() {
        return null;
    }

    @Override
    public IgniteScheduler scheduler() {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> createCache(CacheConfiguration<K, V> cacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public Collection<IgniteCache> createCaches(Collection<CacheConfiguration> collection) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> createCache(String s) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> getOrCreateCache(CacheConfiguration<K, V> cacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> getOrCreateCache(String s) throws CacheException {
        return null;
    }

    @Override
    public Collection<IgniteCache> getOrCreateCaches(Collection<CacheConfiguration> collection) throws CacheException {
        return null;
    }

    @Override
    public <K, V> void addCacheConfiguration(CacheConfiguration<K, V> cacheConfiguration) throws CacheException {

    }

    @Override
    public <K, V> IgniteCache<K, V> createCache(CacheConfiguration<K, V> cacheConfiguration, NearCacheConfiguration<K, V> nearCacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> getOrCreateCache(CacheConfiguration<K, V> cacheConfiguration, NearCacheConfiguration<K, V> nearCacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> createNearCache(String s, NearCacheConfiguration<K, V> nearCacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public <K, V> IgniteCache<K, V> getOrCreateNearCache(String s, NearCacheConfiguration<K, V> nearCacheConfiguration) throws CacheException {
        return null;
    }

    @Override
    public void destroyCache(String s) throws CacheException {

    }

    @Override
    public void destroyCaches(Collection<String> collection) throws CacheException {

    }

    @Override
    public <K, V> IgniteCache<K, V> cache(String s) throws CacheException {
        return null;
    }

    @Override
    public Collection<String> cacheNames() {
        return null;
    }

    @Override
    public IgniteTransactions transactions() {
        return null;
    }

    @Override
    public <K, V> IgniteDataStreamer<K, V> dataStreamer(String s) throws IllegalStateException {
        return null;
    }

    @Override
    public IgniteFileSystem fileSystem(String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<IgniteFileSystem> fileSystems() {
        return null;
    }

    @Override
    public IgniteAtomicSequence atomicSequence(String s, long l, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public IgniteAtomicSequence atomicSequence(String s, AtomicConfiguration atomicConfiguration, long l, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public IgniteAtomicLong atomicLong(String s, long l, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public IgniteAtomicLong atomicLong(String s, AtomicConfiguration atomicConfiguration, long l, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public <T> IgniteAtomicReference<T> atomicReference(String s, @Nullable T t, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public <T> IgniteAtomicReference<T> atomicReference(String s, AtomicConfiguration atomicConfiguration, @Nullable T t, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public <T, S> IgniteAtomicStamped<T, S> atomicStamped(String s, @Nullable T t, @Nullable S s1, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public <T, S> IgniteAtomicStamped<T, S> atomicStamped(String s, AtomicConfiguration atomicConfiguration, @Nullable T t, @Nullable S s1, boolean b) throws IgniteException {
        return null;
    }

    @Override
    public IgniteCountDownLatch countDownLatch(String s, int i, boolean b, boolean b1) throws IgniteException {
        return null;
    }

    @Override
    public IgniteSemaphore semaphore(String s, int i, boolean b, boolean b1) throws IgniteException {
        return null;
    }

    @Override
    public IgniteLock reentrantLock(String s, boolean b, boolean b1, boolean b2) throws IgniteException {
        return null;
    }

    @Override
    public <T> IgniteQueue<T> queue(String s, int i, @Nullable CollectionConfiguration collectionConfiguration) throws IgniteException {
        return null;
    }

    @Override
    public <T> IgniteSet<T> set(String s, @Nullable CollectionConfiguration collectionConfiguration) throws IgniteException {
        return null;
    }

    @Override
    public <T extends IgnitePlugin> T plugin(String s) throws PluginNotFoundException {
        return null;
    }

    @Override
    public IgniteBinary binary() {
        return null;
    }

    @Override
    public void close() throws IgniteException {

    }

    @Override
    public <K> Affinity<K> affinity(String s) {
        return null;
    }

    @Override
    public boolean active() {
        return false;
    }

    @Override
    public void active(boolean b) {

    }

    @Override
    public void resetLostPartitions(Collection<String> collection) {

    }

    @Override
    public Collection<MemoryMetrics> memoryMetrics() {
        return null;
    }

    @Nullable
    @Override
    public MemoryMetrics memoryMetrics(String s) {
        return null;
    }

    @Override
    public PersistenceMetrics persistentStoreMetrics() {
        return null;
    }
}
