
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Lam Du Thach
 * @param <T>
 */
public abstract class ObjectPool<T> {

    private volatile long expirationTime;

    private final Hashtable<T, Long> inUseConn, useableConn;

    public ObjectPool() {
        expirationTime = 30000; // 30 seconds
        inUseConn = new Hashtable<>();
        useableConn = new Hashtable<>();
    }

    protected abstract T create();

    protected abstract boolean validate(T o);

    protected abstract void expire(T o);

    protected synchronized T getConn() {
        long now = System.currentTimeMillis();
        T t;
        if (useableConn.size() > 0) {
            Enumeration<T> e = useableConn.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                if ((now - useableConn.get(t)) > expirationTime) {
                    // object has expired
                    useableConn.remove(t);
                    expire(t);
                    t = null;
                } else {
                    if (validate(t)) {
                        useableConn.remove(t);
                        inUseConn.put(t, now);
                        return t;
                    }
                    // object failed validation
                    useableConn.remove(t);
                    expire(t);
                    t = null;
                }
            }
        }
        // no objects available, create new one
        t = create();
        inUseConn.put(t, now);
        return t;
    }

    public synchronized void releaseConn(T t) {
        inUseConn.remove(t);
        useableConn.put(t, System.currentTimeMillis());
    }
}
