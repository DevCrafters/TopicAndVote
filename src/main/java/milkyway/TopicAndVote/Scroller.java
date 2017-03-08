package milkyway.TopicAndVote;



import java.util.ArrayList;
import java.util.List;

public class Scroller
{
    private static final char COLOUR_CHAR = '¡×';
    private int position;
    private List<String> list;

    public Scroller(String message, int width, int spaceBetween)
    {
        list = new ArrayList<String>();
 
        // Validation
        // String is too short for window
        if (message.length() < width)
        {
            /* sb = new StringBuilder(message);
            while (sb.length() < width)
                sb.append(" ");
            message = sb.toString();*/
        	list.add(message.replaceAll("&([0-9a-fA-F])", "¡×$1"));
        	return;
        }
 
        // Allow for colours which add 2 to the width
        width -= 2;
 
        // Invalid width/space size
        if (width < 1)
            width = 1;
        if (spaceBetween < 0)
            spaceBetween = 0;
 

        // Add substrings
        for (int i = 0; i < message.length() - width; i++)
            list.add(message.substring(i, i + width));
 
        // Add space between repeats
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < spaceBetween; ++i)
        {
            list.add(message.substring(message.length() - width + (i > width ? width : i), message.length()) + space);
            if (space.length() < width)
                space.append(" ");
        }
 
        // Wrap
        for (int i = 0; i < width - spaceBetween; ++i)
            list.add(message.substring(message.length() - width + spaceBetween + i, message.length()) + space + message.substring(0, i));
 
        // Join up
        for (int i = 0; i < spaceBetween; i++)
        {
            if (i > space.length())
                break;
            list.add(space.substring(0, space.length() - i) + message.substring(0, width - (spaceBetween > width ? width : spaceBetween) + i));
        }
    }
 
    /**
    * @return Gets the next String to display
    */
    public String next()
    {
    	if(list.size() == 1)
    		return list.get(0);
        StringBuilder sb = getNext();
        if (sb.charAt(sb.length() - 1) == COLOUR_CHAR)
            sb.setCharAt(sb.length() - 1, ' ');
 
        return sb.toString();
 
    }
 
    private StringBuilder getNext()
    {

        return new StringBuilder(list.get(position++ % list.size()).substring(0));
    }
 
}