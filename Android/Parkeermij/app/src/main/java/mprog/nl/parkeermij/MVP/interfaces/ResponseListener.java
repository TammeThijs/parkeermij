package mprog.nl.parkeermij.MVP.interfaces;

/**
 * Created by Tamme on 4-9-2015.
 */
public interface ResponseListener<T> {

    void success(T response);

    void fail(String error);
}
