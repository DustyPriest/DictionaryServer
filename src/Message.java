import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -2723363051271966964L;

    private final Status status;
    private final String[] data;

    public Message(Status status, String[] data) {
        this.status = status;
        this.data = data;
    }

    public Message (Status status, String data) {
        this.status = status;
        this.data = new String[]{data};
    }

    public Message (Status status) {
        this.status = status;
        this.data = null;
    }

    public Status getStatus() {
        return status;
    }

    public String[] getData() {
        return data;
    }
}
