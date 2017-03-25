/**
 * C202 - AssignmentFour.java
 * @author Nathan Smith
 * @version 1.0 3/23/17
 * 
 * Purpose: to implement and test dictionary spellchecking
 * using the MyLinkedList created in Lab6
 */

import java.io.*;
import java.util.Scanner;

public class AssignmentFour {
    private MyLinkedList<String>[] dictionary = new MyLinkedList[26]; //array of MyLinkedLists
    private long found = 0; //number of words found
    private long foundComps = 0; //number of comparisons of words found
    private long notFound = 0; //number of words not found 
    private long notFoundComps = 0; //number of comparisons of words not found

    /**
     * Default constructor that loads the dictionary
     * array with MyLinkedLists
     */
    public AssignmentFour() {
        for(int i = 0; i < dictionary.length; i++)
            dictionary[i] = new MyLinkedList<String>();
    }

    /**
     * Precondition: a dictionary File and an array of length 26
     * Postcondition: the MyLinkedLists are loaded
     * with every word of the appropriate letter
     * in the File
     */
    public void loadDictionary() {
        File dict = new File("random_dictionary.txt");
        try {
            Scanner in = new Scanner(dict);
            while(in.hasNext()) {
                String word = in.next().toLowerCase();
                dictionary[word.charAt(0) - 97].add(word);
            }
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Precondition: a book file and a loaded dictionary
     * Postcondition: the number of words found and not found,
     * the number of comparisons of words found and not found,
     * and the average number of comparisons of words found 
     * and not found
     *
     * Uses overloaded contains method and checks two special
     * cases concerning hyphens and apostrophes
     */
    public void spellCheck() {
        File book = new File("oliver.txt");
        int[] i = {0}; //passed into comatains method to hold number of comaprisons
        
        try {
            Scanner in = new Scanner(book);
            while(in.hasNext()) {
                String word = in.next().toLowerCase().replaceAll("[^a-z\\-']", ""); //eliminate all non a-z, hyphens, or apostrophes
                
                if(word.isEmpty()) //was not a valid word to check 
                    continue; //this is done to keep the notFound from adding tons of unnecessary words
                
                if(word.contains("-")) {
                    for(String words : word.split("-")) { //get everything on either side of the hyphens
                        String test = words.replaceAll("('[a-z]+)|[^a-z]", ""); //replace all non a-z to the right of the apostrophe,   
                        if(test.isEmpty())                                      //further replace any non a-z after that
                            continue;   
                        if(dictionary[test.charAt(0) - 97].contains(test, i)) {
                            found++;
                            foundComps += i[0];
                        }
                        else {
                            notFound++;
                            notFoundComps += i[0];
                        }
                    }
                }

                else if(word.contains("'")) {
                    String front = word.replaceAll("('[a-z]+)|[^a-z]", ""); //see above
                    if(front.isEmpty())
                        continue;
                    if(dictionary[front.charAt(0) - 97].contains(front, i)) {
                        found++;
                        foundComps += i[0];
                    }
                    else {
                        notFound++;
                        notFoundComps += i[0];
                    }
                }
                
                else if(dictionary[word.charAt(0) - 97].contains(word, i)) {
                    found++;
                    foundComps += i[0];
                }
                
                else {
                    notFound++;
                    notFoundComps += i[0];
                }
            }
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose: tests the methods implemented
     * in AssignmentFour.java
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        long start = System.nanoTime();
        AssignmentFour a = new AssignmentFour();
        a.loadDictionary();
        a.spellCheck();
        
        System.out.println("Words found is " + a.found);
        System.out.println("Words not found is " + a.notFound);
        System.out.println("Number of comparisons of words found is " + a.foundComps);
        System.out.println("Number of comparisons of words not found is " + a.notFoundComps);
        System.out.printf("The average number of comparisons of words found is %.2f", (double) a.foundComps / a.found);
        System.out.printf("\nThe average number of comparisons of words not found is %.2f", (double) a.notFoundComps / a.notFound);
        System.out.printf("\nRuntime: %.4f seconds\n", (System.nanoTime() - start) / Math.pow(10, 9)); 
    }

    /**
     * Overloaded contains method
     * Precondition: an element and an integer array
     * with at least one length
     * Postcondition: true if the element is found, false
     * otherwise. First element of count will be the number
     * of comparisons done
     *
     * @ param e the element to be searched
     * @ param count will store the number of comparisons
     * @ return boolean true if the element is found
     * and false if it is not
     *
     * public boolean contains(E e, int[] count) {
     *     Node<E> currentNode = this.head;
     *     for(int i = 0; i < this.size; i++) {
     *         if(currentNode.element.equals(e)) {
     *             count[0] = i + 1;
     *             return true;
     *         }
     *         currentNode = currentNode.next;
     *     }
     *     count[0] = this.size;
     *     return false;
     * }
     */
}