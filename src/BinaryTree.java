import java.util.ArrayList;

public class BinaryTree {
    
    TreeNode<CharacterF> root;
    
    public BinaryTree() {root = null;};
    public BinaryTree(TreeNode<CharacterF> root) {this.root = root;}

    public void setRoot(TreeNode<CharacterF> root) {this.root = root;}
    public TreeNode<CharacterF> getRoot() {return root;}

    public int getHeight(TreeNode<CharacterF> node) {

        if(node == null) {

            return -1;
        }

        else {

            return 1 + Math.max(getHeight(node.getLeftTree()), getHeight(node.getRightTree()));
        }
    }

    private boolean findPath(TreeNode<CharacterF> node, String target, StringBuilder path) {

        if(node == null) return false;

        if(node.getBinaryValue() < 2) path.append(node.getBinaryValue());

        if(node.getData().getCharacter().equals(target)) return true;

        if(findPath(node.getLeftTree(), target, path) || findPath(node.getRightTree(), target, path)) return true;

        path.deleteCharAt(path.length()-1);
        return false;
    }

    public StringBuilder getPath(String target) {

        StringBuilder path = new StringBuilder();
        if(this.findPath(this.root, target.toLowerCase(), path)) return path;
        else return null;
    }

    private boolean getEquivalent(TreeNode<CharacterF> node, String target, StringBuilder path, StringBuilder letter) {

        if(node == null) return false;

        if(node.getBinaryValue() < 2) path.append(node.getBinaryValue());

        if(path.toString().equals(target)) {letter.append(node.getData().getCharacter()); return true;}

        if(getEquivalent(node.getLeftTree(), target, path, letter) || getEquivalent(node.getRightTree(), target, path, letter)) return true;

        path.deleteCharAt(path.length()-1);
        return false;
    }

    public String getLetter(String target) {

        StringBuilder path = new StringBuilder();
        StringBuilder letter = new StringBuilder();
        if(getEquivalent(root, target, path, letter)) return letter.toString();
        else return null;
    }

    @Override
    public String toString() {

        return toString(this.root);
    }

    private String toString(TreeNode<CharacterF> node) {

        if(node == null) return "";

        StringBuilder sb = new StringBuilder();

        sb.append(node.getData().getCharacter());

        if(node.getLeftTree() != null) sb.append(", ").append(toString(node.getLeftTree()));
        if(node.getRightTree() != null) sb.append(", ").append(toString(node.getRightTree()));

        return sb.toString();
    }
}
