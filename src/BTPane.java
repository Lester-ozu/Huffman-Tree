import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class BTPane extends Pane {
    
    private BinaryTree myBT;
    private double radius = 15, vGap = 40;

    public BTPane(BinaryTree myBT) {

        this.myBT = myBT;
    }

    public void displayTree() {

        this.getChildren().clear();
        if(myBT.getRoot() != null) {

            displayTree(myBT.getRoot(), getWidth() / 2, vGap, getWidth() / 4, Color.MEDIUMPURPLE);
        }
    }

    protected void displayTree(TreeNode<CharacterF> root, double x, double y, double hGap, Color color) {
    
        if(root.getLeftTree() != null) {

            getChildren().add(new Line(x - hGap, y + vGap, x, y));
            Text leftLabel = new Text((x + (x - hGap - 10)) / 2, ((y + (y + vGap + 10)) / 2) - 5, "0");
            leftLabel.setStyle("-fx-font-size: 7px;");
            getChildren().add(leftLabel);
            displayTree(root.getLeftTree(), x - hGap, y + vGap, hGap / 2, color);
        }

        if(root.getRightTree() != null) {

            getChildren().add(new Line(x + hGap, y + vGap, x, y));
            Text rightLabel = new Text((x + (x + hGap + 5)) / 2, ((y + (y + vGap + 10)) / 2) - 5, "1");
            rightLabel.setStyle("-fx-font-size: 7px;");
            getChildren().add(rightLabel);
            displayTree(root.getRightTree(), x + hGap, y + vGap, hGap / 2, color);
        }

        Circle circle = new Circle(x, y, radius);
        circle.setFill(color);
        circle.setStroke(Color.BLACK);

        Text myText;

        if(!root.getData().getCharacter().matches("^[a-zA-Z]+$")) 
            myText = new Text(x - 5, y + 4, root.getData().getCharacter() + "");
        else
            myText = new Text(x - 10, y + 4, root.getData().getCharacter() + " (" + root.getData().getFrequency() + ")");

        myText.setStyle("-fx-font-size: 10px;");
        getChildren().addAll(circle, myText);
    }
}
