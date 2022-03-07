package adapter;

/** Assignment 4: we have added the scale package
 * which contains an interface scale.Scaleable <BR>
 * Assignment 5: we have added the server package
 * which contains an interface server.AutoServer <BR>
 */
public class BuildAuto extends ProxyAutomobile
	implements CreateAuto, UpdateAuto, ChooseAuto, scale.Scaleable, server.AutoServer {
}
