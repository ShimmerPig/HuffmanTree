package HuffmCompress;

public class HuffmNode {
	//数据域--出现次数
	private int data;
	//索引--也就是这个字符对应的ascii码（用它来作为数组的下标）
	private int index;
	private HuffmNode left;
	private HuffmNode right;
	public HuffmNode(int data, int index) {
		super();
		this.data = data;
		this.index = index;
	}
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public HuffmNode getLeft() {
		return left;
	}
	public void setLeft(HuffmNode left) {
		this.left = left;
	}
	public HuffmNode getRight() {
		return right;
	}
	public void setRight(HuffmNode right) {
		this.right = right;
	}
	
}
