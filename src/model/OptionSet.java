package model;
import java.util.*;
import exception.AutoException;


class OptionSet implements java.io.Serializable {
    private static final long serialVersionUID = 5846223453457830887L;
    ArrayList<Option> optionList;
    private String optionSetName;

    /* Constructor */
    protected OptionSet() {
        init();
        optionList = new ArrayList<Option>();
    }

    protected OptionSet(String name, int size) {
        init();
        optionSetName = name;
        optionList = new ArrayList<Option>(12);
    }

    protected void init() {
        optionSetName = "";
    }

    /* Getter */
    protected String getName() {
        return optionSetName;
    }

    /* get option by option index */
    protected Option getOption(int optionIndex) throws AutoException {
        Option optionObject = null;
        try {
            optionObject = optionList.get(optionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new exception.AutoException(803);
        }
        return optionObject;
    }

    protected int length() {
        return optionList.size();
    }

    /** Find option by name
     * @param optionName option name
     * @return null if not found and option object if found */
    protected Option findOption(String optionName) {
        Option optionObject = null;
        for (int i = 0; i < optionList.size(); i++) {
            try {
                if (optionList.get(i).getName().equals(optionName)) {
                    optionObject = optionList.get(i);
                }
            } catch (NullPointerException e) {
                /* According to Carnegie Mellon University Software Engineering Institute You
                 * should not catch a null pointer exception. BUT WE WILL FOR THE SAKE OF THE
                 * ASSIGNMENT! */
                break;
            }
        }
        return optionObject;
    }

    /** Find option by name
     * @param optionName option name
     * @return -1 if not found and position index if found */
    protected int findOptionIndex(String optionName) {
        int returnValue, i, n;
        returnValue = -1;
        n = optionList.size();
        for (i = 0; i < n; i++) {
            try {
                if (optionList.get(i).getName().equals(optionName)) {
                    returnValue = i;
                    break;
                }
            } catch (NullPointerException e) {
                /* According to Carnegie Mellon University Software Engineering Institute You
                 * should not catch a null pointer exception. BUT WE WILL FOR THE SAKE OF THE
                 * ASSIGNMENT! */
                break;
            }
        }
        return returnValue;
    }

    /* Setter */
    protected void setName(String name) {
        optionSetName = name;
    }

    /** add an option
     * @param optionName option name
     * @param optionPrice option price **/
    protected int addOption(String optionName, double optionPrice) {
        optionList.add(new Option(optionName, optionPrice));
        return optionList.size();
    }

    /* print() and toString() */
    public void print() {
        System.out.print(toString());
    }

    public String toString() {
        StringBuffer stringBufferObject;
        int i, n;
        n = length();
        stringBufferObject = new StringBuffer("");
        stringBufferObject.append(getName()).append(": ");
        for (i = 0; i < n; i++) {
            try {
                stringBufferObject.append(getOption(i).toString());
            } catch (AutoException e) {
                // nothing
            }
            if (i < n - 1) {
                stringBufferObject.append(", ");
            }
        }
        return stringBufferObject.toString();
    }
}