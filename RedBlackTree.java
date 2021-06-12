import java.util.*; 
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections; 
import java.util.concurrent.TimeUnit;
class Node {
  int data;
  Node parent;
  Node left;
  Node right;
 int color;//1 red 0 black
  Node pred;
  Node succ;
}

public class RedBlackTree {
  private Node root;
  private Node TNULL;
  
  

  // Inorder
private void inOrderHelper(Node node) {
   
    Stack<Node> nodes = new Stack<>(); 
      Node current = node; 
    while (!nodes.isEmpty() || current !=TNULL) { 
      
        if (current != TNULL) { 
            nodes.push(current); 
            current = current.left;
            } 
        else {
            Node node1 = nodes.pop(); 
            if(node1.color == 0){
              System.out.print(node1.data + "(BLACK) " + node1.pred.data + "(pred)" + node1.succ.data + "(succ)");
            }else if (node1.color == 1){
              System.out.print(node1.data + "(RED) " + node1.pred.data + "(pred)" + node1.succ.data + "(succ)");
            } 
            current = node1.right; 
            } 
      }
      System.out.println();
}

  // Search the tree
private Node searchTreeHelper(Node node, int key) {
    if (node == TNULL || key == node.data) {
      return node;
    }

    if (key < node.data) {
      return searchTreeHelper(node.left, key);
    }
    return searchTreeHelper(node.right, key);
  }

  // Balance the tree after deletion of a node
  private void fixDelete(Node x) {
    Node s;
    while (x != root && x.color == 0) {
      if (x == x.parent.left) {
        s = x.parent.right;
        if (s.color == 1) {
          s.color = 0;
          x.parent.color = 1;
          leftRotate(x.parent);
          s = x.parent.right;
        }

        if (s.left.color == 0 && s.right.color == 0) {
          s.color = 1;
          x = x.parent;
        } else {
          if (s.right.color == 0) {
            s.left.color = 0;
            s.color = 1;
            rightRotate(s);
            s = x.parent.right;
          }

          s.color = x.parent.color;
          x.parent.color = 0;
          s.right.color = 0;
          leftRotate(x.parent);
          x = root;
        }
      } else {
        s = x.parent.left;
        if (s.color == 1) {
          s.color = 0;
          x.parent.color = 1;
          rightRotate(x.parent);
          s = x.parent.left;
        }

        if (s.right.color == 0 && s.right.color == 0) {
          s.color = 1;
          x = x.parent;
        } else {
          if (s.left.color == 0) {
            s.right.color = 0;
            s.color = 1;
            leftRotate(s);
            s = x.parent.left;
          }

          s.color = x.parent.color;
          x.parent.color = 0;
          s.left.color = 0;
          rightRotate(x.parent);
          x = root;
        }
      }
    }
    x.color = 0;
  }

  

  private void deleteNodeHelper(Node node, int key) {
    
    Node x, y;
    Node z = searchTree(key);
    
    if (z.data != key) {
      System.out.println("key not found");
      return;
    }
    Node temp =z;
    Node k1,k2;
    k1=temp.pred;
    k2=temp.succ;
    if(k1!=TNULL && k2!=TNULL){
      k1.succ=k2;
      k2.pred=k1;
    }
    else if(k1==TNULL){
      k2.pred=k1;
    }
    else if(k2==TNULL){
      k1.succ=k2;
    }
    y = z;
    int yOriginalColor = y.color;
    if (z.left == TNULL) {
      x = z.right;
      rbTransplant(z, z.right);
    } else if (z.right == TNULL) {
      x = z.left;
      rbTransplant(z, z.left);
    } else {
      y = z.succ;
      yOriginalColor = y.color;
      x = y.right;
      if (y.parent == z) {
        x.parent = y;
      } else {
        rbTransplant(y, y.right);
        y.right = z.right;
        y.right.parent = y;
      }

      rbTransplant(z, y);
      y.left = z.left;
      y.left.parent = y;
      y.color = z.color;
    }
    if (yOriginalColor == 0) {
      fixDelete(x);
    }
  }

  // Balance the node after insertion
  private void fixInsert(Node k) {
    Node u;
    while (k.parent.color == 1) {
      if (k.parent == k.parent.parent.right) {
        u = k.parent.parent.left;
        if (u.color == 1) {
          u.color = 0;
          k.parent.color = 0;
          k.parent.parent.color = 1;
          k = k.parent.parent;
        } else {
          if (k == k.parent.left) {
            k = k.parent;
            rightRotate(k);
          }
          k.parent.color = 0;
          k.parent.parent.color = 1;
          leftRotate(k.parent.parent);
        }
      } else {
        u = k.parent.parent.right;

        if (u.color == 1) {
          u.color = 0;
          k.parent.color = 0;
          k.parent.parent.color = 1;
          k = k.parent.parent;
        } else {
          if (k == k.parent.right) {
            k = k.parent;
            leftRotate(k);
          }
          k.parent.color = 0;
          k.parent.parent.color = 1;
          rightRotate(k.parent.parent);
        }
      }
      if (k == root) {
        break;
      }
    }
    root.color = 0;
  }

  private void printHelper(Node root, String indent, boolean last) {
    if (root != TNULL) {
      System.out.print(indent);
      if (last) {
        System.out.print("R----");
        indent += "   ";
      } else {
        System.out.print("L----");
        indent += "|  ";
      }

      String sColor = root.color == 1 ? "RED" : "BLACK";
      System.out.println(root.data + "(" + sColor + ")");
      printHelper(root.left, indent, false);
      printHelper(root.right, indent, true);
    }
  }

  public RedBlackTree() {
    TNULL = new Node();
    TNULL.color = 0;
    TNULL.left = null;
    TNULL.right = null;
    TNULL.pred=null;
    TNULL.succ=null;
    root = TNULL;
  }

 
  public void inorder() {
    inOrderHelper(this.root);
  }

  
public Boolean contains(int key){
  Node node = searchTree(key);
  while(node.data > key){
    node = node.pred;
  }
  while(node.data<key){
    node = node.succ;
  }
  return (node.data == key);
}
  
public Node searchTree(int k) {
    return searchTreeHelper(this.root, k);
  }

  
  
  public void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;
    
    if (y.left != TNULL) {
      y.left.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.left) {
      x.parent.left = y;
    } else {
      x.parent.right = y;
    }
    y.left = x;
    x.parent = y;
  }

  public void rightRotate(Node x) {
    Node y = x.left;
    x.left = y.right;
    if (y.right != TNULL) {
      y.right.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.right) {
      x.parent.right = y;
    } else {
      x.parent.left = y;
    }
    y.right = x;
    x.parent = y;
  }

  public Node searchNode(Node node){
    Node y = null;
    Node x = this.root;

    while (x != TNULL) {
      y = x;
      if (node.data < x.data) {
        x = x.left;
      }
      
      else if(node.data > x.data) {
        x = x.right;
      }
      else{
        return node;
      }
    }
    return y;
  }

  public void insert(int key) {

    Node node = new Node();
    node.parent = null;
    node.data = key;
    node.left = TNULL;
    node.right = TNULL;
    node.color = 1;
    node.pred=TNULL;
    node.succ=TNULL;
    
    Node y = searchNode(node);
    if (y == node){
      System.out.println("key already exsist cant insert duplicate keys");
      return ;
    }
    Node temp;
    node.parent = y;
    if (y == null) {
      root = node;
      node.pred=TNULL;
      node.succ=TNULL;
    } else if (node.data < y.data) {
      y.left = node;
      temp = y.pred;
      y.pred = node ;
      node.succ = y;
      node.pred = temp;
    } else {
      y.right = node;
      temp = y.succ;
      y.succ = node;
      node.pred = y;
      node.succ = temp;
    }
   
    if (node.parent == null) {
      node.color = 0;
      
      return;
    }

    if (node.parent.parent == null) {
        
      return;
    }
    fixInsert(node);
    
  }

  public Node getRoot() {
    return this.root;
  }

  public void deleteNode(int data) {
    deleteNodeHelper(this.root, data);
  }

  public void printTree() {
    printHelper(this.root, "", true);
  }

  public static void main(String[] args) {
    RedBlackTree bst = new RedBlackTree();
    
    int randomNumber = 1000000;
   
       
      long startTime,endTime,timeElapsed,durationInMillis;
   
    for(int i=randomNumber;i>0;i--){
      bst.insert(i);
    }
    
 
        // get the difference between the two nano time valuess
    

        

    startTime = System.nanoTime();
    for(int i=randomNumber;i>0;i--){
      bst.contains(i);
    }
    endTime = System.nanoTime();
 
        // get the difference between the two nano time valuess
    timeElapsed = endTime - startTime;
    durationInMillis = TimeUnit.NANOSECONDS.toMillis(timeElapsed);
   System.out.println(timeElapsed);
    System.out.println( durationInMillis);



   /* startTime = System.nanoTime();
    for(int i=0;i<randomNumber;i++){
      bst.contains(i);
    }
    endTime = System.nanoTime();
 
        // get the difference between the two nano time valuess
    timeElapsed = endTime - startTime;
    durationInMillis = TimeUnit.NANOSECONDS.toMillis(timeElapsed);
    System.out.println(timeElapsed);
    System.out.println("Execution time in milliseconds: " + durationInMillis);*/
        


   /* Scanner sc= new Scanner(System.in);
    try{
      int choice = 1;             
      
      while(choice!=5){
        System.out.println("1)INSERT ");
        System.out.println("2)DELETE");
        System.out.println("3)SEARCH");
        System.out.println("4)PRINT");
        System.out.println("5)EXIT");
      System.out.println("Input your choice : ");
      choice = sc.nextInt();
        switch (choice){
          case 1:
                System.out.print("enter the number to be inserted:");
                bst.insert(sc.nextInt());
                break;
          case 2:
              System.out.print("enter the number to be deleted : ");
              bst.deleteNode(sc.nextInt());
              break;
          case 3:
              System.out.print("enter the number to be searched:");
              Boolean contain = bst.contains(sc.nextInt());
              if(contain){
                System.out.println("Key found");
              } else{
                System.out.println("Key doesn't exist");
              }
              break;
          case 4:
          bst.printTree();
          System.out.println("printing tree in logical order");
          bst.inorder();
          break;
          default:
              System.out.println("Please enter valid choice");
        }
      }
    }finally{
      sc.close();
    }*/
  
  }
}