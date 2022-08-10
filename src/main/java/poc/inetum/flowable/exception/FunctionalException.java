package poc.inetum.flowable.exception;

public class FunctionalException extends Exception {

    /**
     * Serial Id
     */
    private static final long serialVersionUID = 7664517780593149849L;

    public FunctionalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FunctionalException(final String message) {
        super(message);
    }

}
