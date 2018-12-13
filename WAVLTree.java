
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * WAVLTree
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {

	private WAVLNode root;
	private WAVLNode minNode;
	private WAVLNode maxNode;
	
	public WAVLTree() {
		this.root = null;
		this.minNode = null;
		this.maxNode = null;
	}
	
  /**
	    * public int getRoot()
	    * Returns the root WAVL node, or null if the tree is empty
	    * precondition: none
	    * postcondition: none
	    * Complexity of O(1)
	    */
	   public IWAVLNode getRoot()
	   {
		   return this.root;
	   }
	   /**
	    * public void setRoot(WAVLNode root)
	    *
	    * Sets the WAVLTree root and delete any parent the root might had 
	    *
	    * precondition: none
	    * Complexity of O(1)
	    */
	public void setRoot(WAVLNode root)
	   {
		   this.root = root;
		   if(root != null) {
			   root.setParent(null);
		   }
	   }

/**
	    * public int size()
	    *
	    * Returns the number of nodes in the tree.
	    *
	    * precondition: none
	    * postcondition: none
	    * Complexity of O(1)
	    */
	   public int size()
	   {
		   if(this.getRoot() == null) {
			   return 0;
		   }
		   return this.getRoot().getSubtreeSize();
	   }

/**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *Complexity O(1)
   */
  public boolean empty() {
	  if(this.getRoot()==null)
		  return true;
	  return (!this.getRoot().isRealNode());
  }

 /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    * Complexity O(log n), maximal amount of iterations
    * is the height of the tree
    */
   public String min() {
	   if(this.minNode == null) {
		   return null;
	   }
	   return this.minNode.getValue();
   }

/**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    * Complexity O(log n), maximal amount of interations
    * is the height of the tree
    */
   public String max() {
	   if(this.maxNode == null) {
		   return null;
	   }
	   return this.maxNode.getValue();
   }

/**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   * Complexity O(log n), maximal amount of iterations
   * is the height of the tree +1
   */
  public String search(int k)
  {
	IWAVLNode node = this.getRoot();
	while(node!=null && node.isRealNode()) {
		if(node.getKey()==k) {
			return node.getValue();
		}
		if(node.getKey()>k) {
			node=node.getLeft();
		}
		else {
			node=node.getRight();
		}
	}
	return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   * Complexity O()
   */
   
   public int insert(int k, String i) {
	   /* func findInsertionPnt (if k already exists return the node with k as key)*/
	   WAVLNode insertionPntNode = findInsertionPnt(k); 
	   /* insertion as root*/
	   if (insertionPntNode == null) {
		   this.setRoot(new WAVLNode (k,i));
		   return 0;
	   }
	   if (k == insertionPntNode.getKey()) {
		   insertionPntNode.setValue(i); 
		   return -1;
	   }
	   
	   boolean InerstionPntIsLeaf = insertionPntNode.isLeaf(); 
	   WAVLNode insertedNode = new WAVLNode (k,i);
	   
	   // update min / max
	   if(this.minNode == null || k < this.minNode.getKey()) {
		   this.minNode = insertedNode;
	   }
	   if(this.maxNode == null || k > this.maxNode.getKey()) {
		   this.maxNode = insertedNode;
	   }
	   
	   if (k < insertionPntNode.getKey()) {
		   /* add left child*/
		   insertionPntNode.setLeft(insertedNode); 
	   }
	   else {
		   /* add right child*/
		   insertionPntNode.setRight(insertedNode); 
	   }
	   /* update ancestors sub tree size after insertion*/
	   updateSubtreeSize(insertionPntNode, 1);
	   
	   if (InerstionPntIsLeaf) { /*insert to leaf */
		   return rebalance(insertionPntNode,0); /*int func - input : the place we need to balance*/
	   }
	   /*insert to unary node*/
	   return 0;
   }
   /**
    * public void insertionUpdateSubtreeSize(WAVLNode node)
    *
    * updates sub tree sizes following an insertion.
    * Complexity O(log n)
    */
   public void updateSubtreeSize(WAVLNode node, int count) {
	   while(node != null) {
		   node.setSubtreeSize(node.getSubtreeSize() + count);
		   node = node.getParent();
	   }
   }
   
   /**
    * public WAVLNode findInsertionPnt(int)
    * @param key
    * @return parent of the node to add or null if should be inserted as root
    * Complexity O(log n)
    */
   public WAVLNode findInsertionPnt(int key) {
	   WAVLNode x = (WAVLNode) this.getRoot();
	   WAVLNode xSon = (WAVLNode)this.getRoot();
	   while(xSon != null && xSon.isRealNode()) {
		   x = xSon;
		   if(x.getKey() == key) {
			   return x;
		   }
		   if(x.getKey() > key) {
			   xSon = x.getLeft();
		   }
		   else {
			   xSon = x.getRight();
		   }
	   }
	   return x;
   }
   
   /**
    * private int rebalance (WAVLNode currNode, int rebalanceOps)
    *@pre:currNode !=null && currNode.isRealNode()
    * rebalance the tree following an inserton, in order to keep the tree as a WAVLTree.
    * returns the number of rebalancing operations.
    * Complexity O()
    */
   private int rebalance (WAVLNode currNode, int rebalanceOps) {
	   WAVLNode left = currNode.getLeft();
	   WAVLNode right = currNode.getRight();
	   int currRank = currNode.getRank();
	   int leftRank = left.getRank();
	   int rightRank = right.getRank();
	   
	   /* if the tree is valid*/
	   if (currRank != leftRank && currRank != rightRank) {
		   return rebalanceOps;
	   }
	   
	   /*terminal cases - RIGHT ROTATIONS*/
	   if (currRank == leftRank && currRank - rightRank == 2) {
		   /*single right rotation*/
		   if (leftRank - left.getLeft().getRank() == 1 && leftRank - left.getRight().getRank()==2) {
			   singleRightRotation (currNode); 
			   return 1 + rebalanceOps; 
		   }
		   else {
			   /*double left rotation*/
			   doubleRotationLR (currNode); 
			   return 2 + rebalanceOps;
		   }
	   }
	   
	   /*terminal cases - LEFT ROTATIONS*/
	   if (currRank == rightRank && currRank - leftRank == 2) {
		   /*single left rotation*/
		   if (rightRank - right.getRight().getRank() == 1 && rightRank - right.getLeft().getRank() == 2) {
			   singleLeftRotation(currNode);
			   return 1 + rebalanceOps; 
		   }
		   else {
			   doubleRotationRL (currNode);  
			   return 2 + rebalanceOps;
		   }
	   }
	   
	   /* NON terminal cases - Promote*/
	   if ((currRank == leftRank && currRank - rightRank == 1) || ((currRank == rightRank && currRank - leftRank == 1)))  {
		   currNode.promoteRank();  /*set currNode rank +1*/
		   if (currNode == this.getRoot()) {
			   return rebalanceOps + 1;
		   }
		   return rebalance(currNode.getParent(), rebalanceOps + 1); 
	   }
	   /*not suppose to get here*/
	   return 0;
   }  
	 
   /**
    * private void singleRightRotation (WAVLNode z)
    *@pre:z !=null && z.isRealNode()
    * perform a single right rotation.
    * Complexity O( )==get max{subtree size, demote rank}
    */
  private void singleRightRotation (WAVLNode z) 
   {
	   WAVLNode x = z.getLeft();
	   if (z == this.getRoot()) {
		   this.setRoot(x);
	   }
	   else 
	   {
		   if (z.getKey() < z.getParent().getKey()) {
			   /*add x as left child of z's parent*/
			   z.getParent().setLeft(x);
		   }
		   else {
			   /*add x as left child of z's parent*/
			   z.getParent().setRight(x);
		   }
	   }
	   
	   z.setLeft(x.getRight());
	   x.setRight(z);
	   z.demoteRank();
	   rotationSizeUpdate (z,x);
   }
  
  private void delSingleRightRotation (WAVLNode z) 
  {
	   WAVLNode y = z.getLeft();
	   if (z == this.getRoot()) {
		   this.setRoot(y);
	   }
	   else {
		   if (z.getKey() < z.getParent().getKey()) {
			   /*add x as left child of z's parent*/
			   z.getParent().setLeft(y);
		   }
		   else {
			   /*add x as left child of z's parent*/
			   z.getParent().setRight(y);
		   }
	   }
	   z.setLeft(y.getRight());
	   y.setRight(z);
	   z.demoteRank();
	   y.promoteRank();
	   rotationSizeUpdate (z,y);
  }

  /**
   * private void singleLeftRotation (WAVLNode z)
   *@pre:z !=null && z.isRealNode()
   * perform a single left rotation.
   * Complexity O( )==get max{subtree size, demote rank}
   */
private void singleLeftRotation (WAVLNode z) 
   {
	   WAVLNode x = z.getRight();
	   if (z == this.getRoot()) {
		   this.setRoot(x);
	   }
	   else {
		   if (z.getKey() < z.getParent().getKey()) {
			   /*add x as left child of z's parent*/
			   z.getParent().setLeft(x);
		   }
		   else {
			   /*add x as left child of z's parent*/
			   z.getParent().setRight(x);
		   }
	   }
	   z.setRight(x.getLeft());
	   x.setLeft(z);
	   z.demoteRank();
	   rotationSizeUpdate (z,x);
   }

private void delSingleLeftRotation (WAVLNode z) 
{
	   WAVLNode y = z.getRight();
	   if (z == this.getRoot()) {
		   this.setRoot(y);
	   }
	   else {
		   if (z.getKey() < z.getParent().getKey()) {
			   /*add x as left child of z's parent*/
			   z.getParent().setLeft(y);
		   }
		   else {
			   /*add x as left child of z's parent*/
			   z.getParent().setRight(y);
		   }
	   }
	   z.setRight(y.getLeft());
	   y.setLeft(z);
	   z.demoteRank();
	   y.promoteRank();
	   rotationSizeUpdate (z,y);
}

/**
 * doubleRotationLR (WAVLNode z)
 *@pre:z !=null && z.isRealNode()
 * perform a single left rotation and a single right rotation.
 * Complexity O( )== singleLeftrotation
 */
private void doubleRotationLR (WAVLNode z) {
	   this.singleLeftRotation(z.getLeft());
	   this.singleRightRotation(z);
	   z.getParent().promoteRank();
   }

private void delDoubleRotationLR (WAVLNode z) {
	   this.delSingleLeftRotation(z.getLeft());
	   this.delSingleRightRotation(z);
	   z.demoteRank();
}

/**
 * doubleRotationRL (WAVLNode z)
 *@pre:z !=null && z.isRealNode()
 * perform a single right rotation and a single left rotation.
 * Complexity O( )== singleLeftrotation
 */
private void doubleRotationRL (WAVLNode z) {
	   this.singleRightRotation (z.getRight());
	   this.singleLeftRotation(z);
	   z.getParent().promoteRank();
   }

private void delDoubleRotationRL (WAVLNode z) {
	   this.delSingleRightRotation (z.getRight());
	   this.delSingleLeftRotation(z);
	   z.demoteRank();
}
/**
* private static void rotationSizeUpdate (WAVLNode z, WAVLNode x)
* update z and x (the rotated nodes) after single rotation
*@pre:z,x !=null && z,x.isRealNode()
* perform a single right rotation and a single left rotation.
* Complexity O( )== getSubTreeSize
*/
   private static void rotationSizeUpdate (WAVLNode z, WAVLNode x) {
	   x.setSubtreeSize(z.getSubtreeSize());
	   z.setSubtreeSize(z.getRight().getSubtreeSize() + z.getLeft().getSubtreeSize() + 1 );
   }

/**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k) {
	   WAVLNode toDelete = findInsertionPnt(k);
	   if(toDelete==null || !toDelete.isReal || toDelete.getKey() != k) {
		   return -1;
	   }
	   
	   int res = 0;
	   if(toDelete.isLeaf()) {
		   res = deleteLeaf(toDelete);
	   }
	   else if(toDelete.getLeft()==null || !toDelete.getLeft().isRealNode() ||toDelete.getRight()==null || !toDelete.getRight().isRealNode()){
		   res = deleteUnary(toDelete);//deleting an unary node
	   }
	   else {
		   res = deleteBinary(toDelete);
	   }
	   
	   //update min / max
	   if(this.getRoot() == null) {
		   this.minNode = null;
		   this.maxNode = null;
	   }
	   else if(k == this.minNode.getKey()) { 
		   WAVLNode node = (WAVLNode)this.getRoot();
		   while(node.getLeft().isRealNode()) {
			   node = node.getLeft();
		   }
		   this.minNode = node;
	   }
	   else if(k == this.maxNode.getKey()) {
		   WAVLNode node = (WAVLNode)this.getRoot();
		   while(node.getRight().isRealNode()) {
			   node = node.getRight();
		   }
		   this.maxNode = node;
	   }
	   
	   return res;
   }
   
   public int delRebalance(WAVLNode curr, int rebalanceOps) {
	   if(curr == null || !curr.isRealNode()) {
		   return rebalanceOps;
	   }
	   
	   WAVLNode left = curr.getLeft();
	   WAVLNode right = curr.getRight();
	   if(curr.isLeaf() && curr.rankDif(left)==2 && curr.rankDif(right)==2) {
		   curr.demoteRank();
		   return delRebalance(curr.getParent(), rebalanceOps + 1);
	   }
	   
	   if(curr.rankDif(left)==3 || curr.rankDif(right)==3) {
		   //**Case 1** curr is a 2,2 leaf
		   if(curr.rankDif(left)==2 || curr.rankDif(right)==2) {
			   curr.demoteRank();
			   return delRebalance(curr.getParent(), rebalanceOps + 1);
		   }
		   
		   // curr is a 3,1 node and y is the child with diff rank 1
		   boolean leftDiffRank3 = false;
		   WAVLNode y = left;
		   if(curr.rankDif(left) == 3) {
			   leftDiffRank3 = true;
			   y = right;
		   }

		   //**Case 2 - double promote** y is a 2,2 node
		   if(y.rankDif(y.getLeft()) == 2 && y.rankDif(y.getRight()) == 2) {
			   y.demoteRank();
			   curr.demoteRank();
			   return delRebalance(curr.getParent(), rebalanceOps + 2);
		   }
		   
		   //**Case 3** 
		   if(leftDiffRank3 && y.rankDif(y.getRight())==1) {
			   delSingleLeftRotation(curr);
			   if(curr.isLeaf()) {
				   curr.demoteRank();
				   rebalanceOps++;
			   }
			   return rebalanceOps + 1;
		   }
		   if(!leftDiffRank3 && y.rankDif(y.getLeft())==1) {
			   delSingleRightRotation(curr);
			   if(curr.isLeaf()) {
				   curr.demoteRank();
				   rebalanceOps++;
			   }
			   return rebalanceOps + 1;
		   }
		   
		   //**Case 4**
		   if(leftDiffRank3) {
			   delDoubleRotationRL(curr);
		   }
		   else {
			   delDoubleRotationLR(curr);
		   }
		   return rebalanceOps+2;  
	   }  
	   return rebalanceOps;   
   }
   
   public int deleteBinary(WAVLNode toDelete) {
	   WAVLNode suc = toDelete.getRight();
	   while(suc.getLeft() != null && suc.getLeft().isRealNode()) {
		   suc = suc.getLeft();
	   }
	   
	   int rebalanceOps = delete(suc.getKey());
	   toDelete.replace(suc);
	   return rebalanceOps;
   }
   
   public int deleteUnary(WAVLNode toDelete) {
	   WAVLNode x = toDelete.getRight();
	   if(toDelete.getLeft()!=null && toDelete.getLeft().isRealNode()) {
		   x = toDelete.getLeft(); 
	   }
	   
	   if(this.getRoot() == toDelete) {
		   this.setRoot(x);
		   return 0;
	   }
	   
	   boolean isLeft = false; // isLeft = true if toDelete is a left child of z
	   WAVLNode z = toDelete.getParent();
	   if(z.getLeft() == toDelete) {
		   isLeft = true;
	   }
	   
	   if(isLeft) {
		   z.setLeft(x);
	   }
	   else {
		   z.setRight(x);
	   }
	   
	   updateSubtreeSize(z, -1);
	   if(z.rankDif(toDelete) == 1) {   
		   return 0;
	   }
	   
	   // rank(z) - rank(toDelete) = 2
	   WAVLNode u = z.getLeft();
	   if(isLeft) {
		   u = z.getRight();
	   }
	   
	   if(z.rankDif(u) == 2) {
		   z.demoteRank();
		   if(z == this.getRoot()) {
			   return 1;
		   }
		   return delRebalance(z.getParent(), 1);
	   }
	   
	   // rank(z) - rank(u) = 1
	   WAVLNode uLeft = u.getLeft();
	   WAVLNode uRight = u.getRight();
	   //**Case 2 - double demote**
	   if(u.rankDif(uLeft) == 2 && u.rankDif(uRight) == 2) {
		   u.demoteRank();
		   z.demoteRank();
		   return delRebalance(z.getParent(), 2);
	   }
	   
	   //**Case 3 - rotate**
	   if((isLeft && u.rankDif(uRight) == 1) || (!isLeft && u.rankDif(uLeft) == 1)) {
		   if(isLeft) {
			   delSingleLeftRotation(z);
		   }
		   else {
			   delSingleRightRotation(z);
		   }
		   return 1;   
	   }
	   
	   //**Case 4 - double rotate**
	   if(isLeft) {
		   delDoubleRotationRL(z);
	   }
	   else {
		   delDoubleRotationLR(z);
	   }
	   return 2;
   }
   
   public int deleteLeaf(WAVLNode toDelete) {
	   if(toDelete == this.getRoot()) {
		   this.setRoot(null);
		   return 0;
	   }
	   
	   WAVLNode z = toDelete.getParent();
	   boolean leftChild = false; // leftChild = true if toDelete is a left child of z
	   WAVLNode u = z.getLeft();
	   if(z.getLeft() == toDelete) {
		   leftChild = true;
		   u = z.getRight();
	   }
	   
	   if(leftChild) { 
		   z.setLeft(new WAVLNode());
	   }
	   else { 
		   z.setRight(new WAVLNode());
	   }
	   
	   updateSubtreeSize(z, -1);
	   
	   //**Case 1** z is a 1,1 node // rank(u)=0
	   if(z.rankDif(toDelete) == 1 && z.rankDif(u)==1) {  
		   return 0;
	   }
	   
	   //**Case 2** z is a 1,2 node
	   if(z.rankDif(toDelete) == 1 && z.rankDif(u) == 2) {
		   z.demoteRank();
		   if(z.getParent() != null) {
			   return delRebalance(z.getParent(), 1);
		   }
		   return 1;
	   }
	   
	   //**Case 3** z is a 2,2 node
	   if(z.rankDif(toDelete) == 2 && z.rankDif(u) == 2) {
		   z.demoteRank();
		   if(z == this.getRoot()) {
			   return 1;
		   }
		   return delRebalance(z.getParent(), 1);
	   }
	   
	   //**Case 4 - rotate**  z is a 2,1 node
	   if(leftChild && u.getRight().isRealNode()) {
		   delSingleLeftRotation(z);
		   if(z.isLeaf()) {
			   z.demoteRank();
			   return 2;
		   }
		   return 1;
	   }
	   if(!leftChild && u.getLeft().isRealNode()) {
		   delSingleRightRotation(z);
		   if(z.isLeaf()) {
			   z.demoteRank();
			   return 2;
		   }
		   return 1;
	   }
		
	   // **Case 4 - double rotate**  z is a 2,1 node
	   if(leftChild) {
		   delDoubleRotationRL(z);
	   }
	   else {
		   delDoubleRotationLR(z);
	   }
	   return 2;   
   }   

   /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   * Complexity O(n)
   */
   public int[] keysToArray() {
	   int[] arr = new int[this.size()];
	   if (this.size() == 0) {
		   return arr;
	   }
	   List<WAVLNode> lst =getInOrderNodes((WAVLNode) this.getRoot());
	   int i = 0;
	   for (WAVLNode node: lst) {
		   arr[i++] = node.getKey();
	   }
	   return arr;
   }
   
   public List<WAVLNode> getInOrderNodes(WAVLNode node){
	   List<WAVLNode> newLst = new ArrayList<WAVLNode>();
	   if (node.getLeft().isRealNode()) {
		   newLst.addAll(getInOrderNodes(node.getLeft()));
	   }
	   newLst.add(node);
	   if (node.getRight().isRealNode()) {
		   newLst.addAll(getInOrderNodes(node.getRight()));
	   }
	   
	   return newLst;
   }
  
    
  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * Complexity O(n)
   */
  public String[] infoToArray()
  {
	   String[] arr = new String[this.size()];
	   if (this.size() == 0) {
		   return arr;
	   }
	   List<WAVLNode> lst =getInOrderNodes((WAVLNode) this.getRoot());
	   int i = 0;
	   for (WAVLNode node: lst) {
		   arr[i++] = node.getValue();
	   }
	   return arr;
  }
  
  
  
   /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return -1 if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    * Complexity O(log n)
    */   
   public String select(int i)
   {
	   if ((this.empty()||(i>this.size()))) {
		   return "-1";
	   }
	   return selectRec(i-1, this.getRoot());
   }

   public static String selectRec (int i, IWAVLNode x) {
	   int r = x.getLeft().getSubtreeSize();
	   if (i==r) {
		   return x.getValue();
	   }
	   else if (i<r) {
		   return selectRec(i, x.getLeft());
	   }
	   else {
		   return selectRec (i - r - 1,x.getRight());
	   }
	   
   }
   
   /**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IWAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}

   /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IWAVLNode)
   */
  public class WAVLNode implements IWAVLNode{
	  /*
	   * each IWAVLNode has:
	   * key (an Integer). virtual nodes key = -1
	   * info - String of information. virtual nodes info = null
	   * left and right pointers 
	   * subTreeSize - how many real IWAVLNodes are in this node's subTree (including this node)
	   * isReal - is the node real or virtual ?
	   * parent 
	   * rank - default 0 if leaf, -1 if external (unReal)
	   */
        private int key;
        private String info;
        private WAVLNode left;
        private WAVLNode right;
        private int subtreeSize;
        private boolean isReal;
        private WAVLNode parent;
        private int rank;
        
        /*main constructor*/
        public WAVLNode (int key,String info,WAVLNode left, WAVLNode right, boolean isReal, WAVLNode parent){
            this.key = key;
            this.info = info;
            this.left = left;
            this.right = right;
            this.subtreeSize = 0;
            this.isReal = isReal;
            this.parent = parent;
            
            if (isReal) {
            	this.rank=0;
	            if (left != null) {
	            	this.subtreeSize += left.subtreeSize  ;
	            }
	            if (right != null) {
	            	this.subtreeSize += right.subtreeSize;
	            }
	            this.subtreeSize +=1;
            }
            else
            	this.rank=-1;
        }
        
        /* construct new unreal WAVLNode*/
        public WAVLNode () {
        	this(-1,null,null,null,false,null);
        }
        /* construct new real WAVLNode
         * each new REAL node children are 2 unreal nodes
         */
        public WAVLNode (int key, String info) {
        	this (key,info,new WAVLNode(),new WAVLNode(),true,null);
        }
        
        /**
         * public int getKey()
         *
         * Returns the node's key, if !this.isReal return -1
         * Complexity O(1)
         */
		public int getKey()
		{
			if (this.isReal) {
				return this.key;
			}
			return -1;
		}

		/**
         * public String getValue()
         *
         * Returns the node's info, if !this.isReal return null
         * Complexity O(1)
         */
		public String getValue()
		{
			if (this.isReal) {
				return this.info;
			}
			return null;
		}
		
		/**
         * public void setValue(String info)
         *
         * Sets the node's info
         * Complexity O(1)
         */
		public void setValue(String info) 
		{
			this.info = info;
		}
		
		/**
		 * public WAVLNode getLeft()
		 * get the node's left son
		 * @return this.left, if (!this.isReal) return null
		 * Complexity O(1)
		 */
		public WAVLNode getLeft()
		{
			if(this.isReal){
                return this.left; 
            }
            return null;
		}
		
		public void setLeft(WAVLNode leftChild) 
		{	if (leftChild!=null) {
				this.left = leftChild;
				leftChild.setParent(this);
			}
		}
		
		/**
		 *$ret IWAVLNode if real Node, else $ret null
		 */
		public WAVLNode getRight()
		{
			if(this.isReal){
                return this.right; 
            }
            return null;
		}
		
		public void setRight(WAVLNode rightChild) 
		{
			if (rightChild!=null) {
				this.right = rightChild;
				rightChild.setParent(this);
			}
		}
		
		public WAVLNode getParent() 
		{
			return this.parent;
		}
		
		public void setParent(WAVLNode parent)
		{
			this.parent = parent;
		}
		
		public void setRank(int rank) {
			this.rank=rank;
		}
		
		public void promoteRank() {
			setRank(this.getRank()+1);
		}
		
		public void demoteRank() {
			setRank(this.getRank()-1);
		}
		
		public int getRank() {
			return this.rank;
		}
		
		// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public boolean isRealNode()
		{
			return this.isReal;
		}
		
		/**
		 * 
		 * @return true iff its a real node with both childs as Unreal nodes
		 */
		public boolean isLeaf() 
		{
			if (this.isRealNode() && !this.getLeft().isRealNode() && !this.getRight().isRealNode()) {
				return true;
			}
			return false;
		}
		/**
		 * virtual nodes subtree = 0 
		 * real nodes subtree >= 1
		 */
		public int getSubtreeSize()
		{
			return this.subtreeSize;
		}
		
		public void setSubtreeSize(int size) {
			this.subtreeSize = size;
		}
		
		//return difference of ranks with a lower rank node 
		public int rankDif(WAVLNode s) {
			return this.getRank() - s.getRank();
		}
		
		public void replace(WAVLNode other) {
			this.setValue(other.getValue());
			this.key = other.getKey();
		}
  }
 }