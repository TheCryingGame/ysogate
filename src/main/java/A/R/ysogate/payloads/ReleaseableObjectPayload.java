package A.R.ysogate.payloads;

/**
 * @author mbechler
 */
public interface ReleaseableObjectPayload<T> extends ObjectPayload<T> {

    void release(T obj) throws Exception;
}