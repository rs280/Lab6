package scale;

/*
 * Interface kept separate from adapter to create modularity
 */
public interface Scaleable {
	public void operation(int operationNumber, String[] inputArguments);

	public void operationSynchronous(int operationNumber, String[] inputArguments);
}
