public class RendererFactory {

    static final String ERROR_MESSAGE = "Choose a renderer, and start again. \n" +
            "Please choose one of the following [console, none]";
    public RendererFactory(){}

    public Renderer buildRenderer(String type, int size) {
        type = type.toLowerCase();
        switch (type) {
            case "console": {
                return new ConsoleRenderer(size);
            }
            case "none": {
                return new VoidRenderer();
            }
            default: {
                System.out.println(ERROR_MESSAGE);
                return null;
            }

        }
    }

}
