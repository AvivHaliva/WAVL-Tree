# WAVL-Tree
This is a WAVL Tree implementation that was a project for the course "Data Structures" at Tel Aviv University 2017.

#### Wikipedia Definition of WAVL Tree:
  A WAVL tree or weak AVL tree is a self-balancing binary search tree. Like other balanced binary search trees, WAVL trees can handle insertion, deletion, and search operations in time O(log n) per operation.
  What distinguishes WAVL trees from other types of binary search tree is its use of ranks. These are numbers, stored with each node, that provide an approximation to the distance from the node to its farthest leaf descendant. The ranks are required to obey the following properties:
  - Every external node has rank 0.
  - If a non-root node has rank r, then the rank of its parent must be either r + 1 or r + 2.
  - An internal node with two external children must have rank exactly 1. 
  
  #### Operations:
  - **empty()** - return True if and only if the tree is empty.
  - **search(k)** - search for the key k in the tree. if such exists, return the info related to it.
  - **insert(k,i)** - insert key k to the tree with the info i.
  - **delete(k)** - delete key k from the tree, if such exists.
  - **min()** - return the info of the minimal key in the tree.
  - **max()** - return the info of the maximal key in the tree.
  - **keysToArray()** - return a sorted array of the keys in the tree.
  - **infoToArray()** - return a sorted (according to the keys) array of the infos in the tree
  - **size()** - return the number of items in the tree.
  - **select(j)** - return the j-th smallest item in the tree.
  - **getRoot()** - return the root of the tree.
  
  
  
  
