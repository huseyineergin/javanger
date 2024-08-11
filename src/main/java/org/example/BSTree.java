package org.example;

import java.util.LinkedList;

public class BSTree {

    private int size = 0;
    private TreeNode root;
    private static BSTree tree;

    public static BSTree getInstance() {
        if (tree == null) {
            tree = new BSTree();
        }
        return tree;
    }

    public void insert(String username, String password) {
        User u = new User(username, password);

        if (size == 0) {
            root = new TreeNode(u);
        } else {
            TreeNode curr = root;

            while (curr != null) {
                int comparison = u.getUsername().compareTo(curr.user.getUsername());

                if (comparison < 0) {
                    if (curr.left != null) {
                        curr = curr.left;
                    } else {
                        TreeNode node = new TreeNode(u);
                        node.parent = curr;
                        curr.left = node;
                        break;
                    }
                } else if (comparison > 0) {
                    if (curr.right != null) {
                        curr = curr.right;
                    } else {
                        TreeNode node = new TreeNode(u);
                        node.parent = curr;
                        curr.right = node;
                        break;
                    }
                }
            }
        }
        size++;
    }

    public void remove(User u) {
        TreeNode toRemove = findNode(u);

        if (toRemove == null)
            return;

        // Case 1: toRemove has no left child
        if (toRemove.left == null) {
            if (toRemove.parent == null)
                root = toRemove.right;
            else {
                if (u.getUsername().compareTo(toRemove.parent.user.getUsername()) < 0)
                    toRemove.parent.left = toRemove.right;
                else
                    toRemove.parent.right = toRemove.right;
            }
        }
        // Case 2: toRemove has a left child
        else {
            // Find rightMost node in the left subtree of the toRemove
            TreeNode rightMost = toRemove.left;

            while (rightMost.right != null) {
                rightMost = rightMost.right;
            }

            // Change value of toRemove to value of rightMost
            toRemove.user = rightMost.user;

            // Get rid of rightMost node
            if (rightMost.parent.right == rightMost)
                // Make rightMost's parent to point its left
                rightMost.parent.right = rightMost.left;
            else
                // Special Case: rightMost.parent == toRemove (?)
                rightMost.parent.left = rightMost.left;
        }
        size--;
    }

    private TreeNode findNode(User u) {
        TreeNode curr = root;

        while (curr != null) {
            int comparison = u.getUserId().compareTo(curr.user.getUserId());

            if (comparison < 0)
                curr = curr.left;
            else if (comparison > 0)
                curr = curr.right;
            else
                return curr;
        }
        return null;
    }

    public User findUserByUsername(String s) {
        TreeNode curr = root;

        while (curr != null) {
            int comparison = s.compareTo(curr.user.getUsername());

            if (comparison < 0) {
                curr = curr.left;
            } else if (comparison > 0) {
                curr = curr.right;
            } else {
                return curr.user;
            }
        }
        return null;
    }

    public LinkedList<User> getUsers(TreeNode root, LinkedList<User> userList) {
        if (root == null)
            return null;
        getUsers(root.left, userList);
        userList.add(root.user);
        getUsers(root.right, userList);
        return userList;
    }

    public TreeNode getRoot() {
        return root;
    }

    private static class TreeNode {

        User user;
        TreeNode left;
        TreeNode right;
        TreeNode parent;

        public TreeNode(User user) {
            this.user = user;
        }
    }
}
