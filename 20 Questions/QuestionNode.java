public class QuestionNode {

    public QuestionNode yes;
    public QuestionNode no;
    public String nodeData;

//    Constructs an answer QuestionNode with null yes/no.

    public QuestionNode(String data) {
        this(null, null, data);
    }

//    Constructs a question QuestionNode with the given yes and/or no nodes.

    public QuestionNode(QuestionNode yes, QuestionNode no, String data) {
        this.yes = yes;
        this.no = no;
        this.nodeData = data;
    }

}
