package com.fanfish.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.fanfish.app.model.Student;

/**
 * An Implementation of the B-Tree Data Structure (excluding delete operation)
 * 
 * @author wenjun See more
 *         at:https://www.crondose.com/2016/10/introduction-to-the-b-tree-data-structure/
 */
public class BTree<T> {

	public static final int ORDER = 2;
	private Node root;

	// Returns the value to which the specified key is mapped, or null if this tree
	// doesn't contain the key.
	public T get(int key) {
		return get(this.root, key);
	}

	private T get(Node current, int key) {
		if (current == null) {
			return null;
		} else {
			List<Integer> keys = current.keys;
			Integer[] arrKeys = keys.toArray(new Integer[0]);
			SearchHelper helper = new SearchHelper();
			int result = helper.binarySearch(arrKeys, key);
			// new key value is already in current node
			if (result != -1) {
				return (T) current.values.get(result);
			} else {
				// new key value is not in current node
				// go to next level node for finding new key
				int next = offset(arrKeys, key);
				List<Node> children = current.children;
				Node child = children.get(next);
				if (child == null) {
					// the next level node of current node is null (leaf node)
					return null;
				} else {
					return get(child, key);
				}
			}
		}
	}

	private Node contains(Node current, int key) {
		if (current == null) {
			return null;
		} else {
			List<Integer> keys = current.keys;
			Integer[] arrKeys = keys.toArray(new Integer[0]);
			SearchHelper helper = new SearchHelper();
			int result = helper.binarySearch(arrKeys, key);
			// new key value is already in current node
			if (result != -1) {
				return current;
			} else {
				// new key value is not in current node
				// go to next level node for finding new key
				int next = offset(arrKeys, key);
				List<Node> children = current.children;
				Node child = children.get(next);
				if (child == null) {
					// the next level node of current node is null (leaf node)
					return null;
				} else {
					return contains(child, key);
				}
			}
		}
	}

	// Returns true if this tree contains the specified key.
	public boolean contains(int key) {
		if (contains(this.root, key) == null)
			return false;
		return true;
	}

	private Node addRecursive(Node current, int key, T val) {
		if (current == null) {
			root = new Node(key, val);
			return root;
		} else {
			List<Integer> keys = current.keys;
			Integer[] arrKeys = keys.toArray(new Integer[0]);
			SearchHelper helper = new SearchHelper();
			int result = helper.binarySearch(arrKeys, key);
			// new key value is already in current node
			if (result != -1) {
				return current;
			} else {
				// new key value is not in current node
				// go to next level node for finding new key
				int next = offset(arrKeys, key);
				List<Node> children = current.children;
				Node child = children.get(next);
				if (child == null) {
					// the next level node of current node is null (leaf node)
					return insertKey(current, key, val, null, null);
				} else {
					return addRecursive(child, key, val);
				}
			}
		}
	}

	private int offset(Integer[] arr, int key) {
		SearchHelper helper = new SearchHelper();
		int higher = helper.firstGreaterValue(arr, key);
		int lower = helper.firstSmallerValue(arr, key);
		int next = -1;
		if (lower == -1) {
			// new key at leftmost
			next = 0;
		} else if (higher == -1) {
			// new key at rightmost
			next = arr.length;
		} else {
			// the entry of next level node
			next = higher;
		}
		return next;
	}

	private void newLR(Node left, Node right, List<Node> children, int leftIndex, int rightIndex) {
		if (right != null) {
			children.set(rightIndex, right);
		} else {
			children.set(rightIndex, null);
		}
		if (left != null) {
			children.set(leftIndex, left);
		} else {
			children.set(leftIndex, null);
		}
	}

	// insert key&value into specific node
	private Node insertKey(Node node, int key, T val, Node newLeft, Node newRight) {
		if (node == null) {
			node = new Node(key, val);
			newLR(newLeft, newRight, node.children, 0, 1);
			this.root = node;
			return this.root;
		}
		List<Integer> keys = node.keys;
		Integer[] arrKeys = keys.toArray(new Integer[0]);
		SearchHelper helper = new SearchHelper();
		int lower = helper.firstSmallerValue(arrKeys, key);
		int keyIndex = lower + 1;
		Integer[] newArrKeys = new Integer[arrKeys.length + 1];
		System.arraycopy(arrKeys, 0, newArrKeys, 0, keyIndex);
		System.arraycopy(arrKeys, keyIndex, newArrKeys, keyIndex + 1, arrKeys.length - keyIndex);
		newArrKeys[keyIndex] = key;
		List<T> values = node.values;
		T[] arrValues = (T[]) values.toArray();
		T[] newArrValues = (T[]) new Object[arrKeys.length + 1];
		System.arraycopy(arrValues, 0, newArrValues, 0, keyIndex);
		System.arraycopy(arrValues, keyIndex, newArrValues, keyIndex + 1, arrValues.length - keyIndex);
		newArrValues[keyIndex] = val;
		int next = offset(arrKeys, key);
		List<Node> children = node.children;
		Node[] arrChildren = children.toArray(new Node[0]);
		Node[] newArrChildren = new Node[arrChildren.length + 1];
		System.arraycopy(arrChildren, 0, newArrChildren, 0, next);
		System.arraycopy(arrChildren, next + 1, newArrChildren, next + 2, arrChildren.length - next - 1);
		if (newRight != null) {
			newArrChildren[next + 1] = newRight;
		} else {
			newArrChildren[next + 1] = null;
		}
		if (newLeft != null) {
			newArrChildren[next] = newLeft;
		} else {
			newArrChildren[next] = null;
		}
		if (keys.size() < ORDER) {
			// current node is not full
			// insert new key to current node
			node.keys = Arrays.asList(newArrKeys);
			node.values = Arrays.asList(newArrValues);
			node.children = Arrays.asList(newArrChildren);
			return node;
		} else {
			// current node is already full,
			// split the node at its median into two nodes at the same level
			// pushing the median element up by one level
			int middle = helper.median(newArrKeys, key);
			int median = newArrKeys[middle];
			Integer[] leftKeys = new Integer[middle];
			Integer[] rightKeys = new Integer[arrKeys.length - middle];
			System.arraycopy(newArrKeys, 0, leftKeys, 0, middle);
			System.arraycopy(newArrKeys, middle + 1, rightKeys, 0, newArrKeys.length - middle - 1);
			T midValue = newArrValues[middle];
			T[] leftValues = (T[]) new Object[middle];
			T[] rightValues = (T[]) new Object[arrValues.length - middle];
			System.arraycopy(newArrValues, 0, leftValues, 0, middle);
			System.arraycopy(newArrValues, middle + 1, rightValues, 0, newArrValues.length - middle - 1);
			Node[] leftChildren = new Node[leftKeys.length + 1];
			Node[] rightChildren = new Node[rightKeys.length + 1];
			System.arraycopy(newArrChildren, 0, leftChildren, 0, middle + 1);
			System.arraycopy(newArrChildren, middle + 1, rightChildren, 0, newArrChildren.length - middle - 1);
			Node left = new Node(leftKeys, leftValues, leftChildren);
			Node right = new Node(rightKeys, rightValues, rightChildren);
			Arrays.stream(leftChildren).forEach(i -> {
				if (i != null)
					i.parent = left;
			});
			Arrays.stream(rightChildren).forEach(i -> {
				if (i != null)
					i.parent = right;
			});
			Node parentNode = insertKey(node.parent, median, midValue, left, right);
			left.parent = parentNode;
			right.parent = parentNode;
			node = null;
			return contains(this.root, key);
		}
	}

	// Appends the specified element(key&value) to the tree.
	public Node add(int key, T val) {
		return addRecursive(root, key, val);
	}

	class Node<T> {
		Node parent;
		// max size of keys are ORDER
		List<Integer> keys;
		// reference list of next level nodes
		// max size of children=ORDER+1
		List<Node> children;
		// real values;
		List<T> values;

		Node(int key, T value) {
			keys = new ArrayList<Integer>();
			keys.add(key);
			values = new ArrayList<T>();
			values.add(value);
			Node[] init_children = new Node[keys.size() + 1];
			children = Arrays.asList(init_children);
		}

		Node(Integer[] keys, T[] values, Node[] children) {
			this.keys = Arrays.asList(keys);
			this.children = Arrays.asList(children);
			this.values = Arrays.asList(values);
		}
	}

	// Breadth-First Search
	public void traverse() {
		if (root == null) {
			return;
		}
		List<Node> tl = new ArrayList<Node>();
		tl.add(root);
		Queue<List<Node>> nodes = new LinkedList<>();
		nodes.add(tl);
		while (!nodes.isEmpty()) {
			List<Node> list = nodes.remove();
			List<Node> tmpList = new ArrayList<Node>();
			for (int i = 0; i < list.size(); i++) {
				Node<T> node = list.get(i);
				for (int j = 0; j < node.keys.size(); j++) {
					System.out.print(" " + node.keys.get(j));
					if (node.children.get(j) != null) {
						tmpList.add(node.children.get(j));
					}
				}
				if (node.children.get(node.keys.size()) != null) {
					tmpList.add(node.children.get(node.keys.size()));
				}
				System.out.print(" | ");
			}
			if (tmpList.size() > 0)
				nodes.add(tmpList);
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BTree<Student> ins = new BTree<Student>();
		Student s1 = new Student(42, "david", 10);
		Student s2 = new Student(300, "tom", 9);
		Student s3 = new Student(175, "jerry", 8);
		Student s4 = new Student(94, "chris", 10);
		Student s5 = new Student(1, "rose", 7);
		Student s6 = new Student(950, "white", 11);
		Student s7 = new Student(250, "trump", 9);
		Student s8 = new Student(100, "jimmy", 15);
		Student s9 = new Student(105, "henry", 7);
		Student s10 = new Student(130, "jordan", 11);
		Student s11 = new Student(160, "wenjun", 9);
		Student[] students = { s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11 };
		int key = 100;
		Arrays.stream(students).forEach(i -> ins.add(i.getPk(), i));
		System.out.println("key: " + ins.contains(key));
		System.out.println("value: " + ins.get(key));
		ins.traverse();
	}
}
