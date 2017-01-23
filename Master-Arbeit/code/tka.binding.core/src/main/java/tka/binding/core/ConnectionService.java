/**
 *
 */
package tka.binding.core;

/**
 * @author Konstantin
 *
 */
public interface ConnectionService {
    /**
     * @return the URL which is used by the user to grant access to his application
     */
    public Object requestAuthorization();

    /**
     * @param information
     * @return <code>true</code>, if the service can confirm that authorization was granted successfully.
     */
    public boolean authorizationGrantedCallback(Object information);

    /**
     * @return <code>true</code>, if the connection service has authorization to act on behalf of the user
     */
    public boolean isAuthorized();

}
