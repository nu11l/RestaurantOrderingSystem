package DataStructures;

import java.io.Serializable;

public class Pair<A, B> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private A first;
	private B second;
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	public String toString() {
		return "(" + this.first + ", " + this.second + ")";
	}
	public A getFirst() {
		return this.first;
	}
	public B getSecond() {
		return this.second;
	}
	public void setFirst(A first) {
		this.first = first;
	}
	public void setSecond(B second) {
		this.second = second;
	}
}
