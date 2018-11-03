package HuffmCompress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.GeneralSecurityException;
import java.util.LinkedList;

import javax.swing.text.ChangedCharSetException;

//压缩以及压缩的实现类
public class Compress {
	//存储出现次数的数组
	public int [] times=new int [256];
	//string类型的数组，存储每个叶子节点的哈夫曼编码
	public String [] HuffmCodes=new String[256];
	//用来存储结点的list
	public LinkedList<HuffmNode>list=new LinkedList<>();
	
	//对每个叶子节点的编码进行初始化
	public Compress() {
		for(int i=0;i<HuffmCodes.length;i++) {
			HuffmCodes[i]="";
		}
	}
	
	//统计每个字符出现次数
	//path为要统计的文件所在的路径
	public void countTimes(String path)throws Exception{
		//文件输入流
		FileInputStream fis=new FileInputStream(path);
		//文件中的是字符，这里得到的是这个字符对应的ascii码，用它来作为数组中的下标
		int value=fis.read();
		while(value!=-1) {
			times[value]++;
			value=fis.read();
		}
		fis.close();
	}
	
	//构造哈夫曼树  返回根节点
	public HuffmNode createTree() {
		for(int i=0;i<times.length;i++) {
			if(times[i]!=0) {
				HuffmNode node=new HuffmNode(times[i], i);
				//插入list中的正确位置--让其变成一个从小到大排序的list
				list.add(getIndex(node),node);
			}
		}
		//构造哈夫曼树
		while(list.size()>1) {
			HuffmNode firstNode=list.removeFirst();
			HuffmNode secondNode=list.removeFirst();
			//索引设置为-1
			HuffmNode father=new HuffmNode(firstNode.getData()+secondNode.getData(), -1);
			father.setLeft(firstNode);
			father.setRight(secondNode);
			list.add(getIndex(father),father);
		}
		return list.getFirst();
	}
	
	//前序遍历获取哈弗们编码
	public void getHuffmCode(HuffmNode root,String code) {
		if(root.getLeft()!=null) {
			getHuffmCode(root.getLeft(), code+"0");
		}
		if(root.getRight()!=null) {
			getHuffmCode(root.getRight(), code+"1");
		}
		//叶子节点--这里的叶子节点中的index是一定正确的  不需要理会其他的结点，只有叶子节点的会被保存进codes数组
		if(root.getLeft()==null&&root.getRight()==null) {
			HuffmCodes[root.getIndex()]=code;
		}
	}
	
	//压缩文件
	//压缩path中的文件，将压缩后的文件output到destpath中
	public void compress(String path,String destpath)throws Exception{
		FileOutputStream fos=new FileOutputStream(destpath);
		FileInputStream fis=new FileInputStream(path);
		
		//将整个哈夫曼编码以及每个编码的长度写入文件中
		//作为解压时候的密码本
		String code="";
		for(int i=0;i<256;i++) {
			//这里只是将程度写入
			//并且对编码进行累加
			fos.write(HuffmCodes[i].length());
			code+=HuffmCodes[i];
			fos.flush();
		}
		
		//将累加猴的哈夫曼编码写入文件中（按照8个一组的形将其转换成10进制后写入），不足8个补0
		String str1="";
		while(code.length()>=8) {
			str1=code.substring(0,8);
			int c=changeStringToInt(str1);
			fos.write(c);
			fos.flush();
			code=code.substring(8);
		}
		int last=8-code.length();
		for(int i=0;i<last;i++) {
			code+="0";
		}
		str1=code.substring(0,8);
		int c=changeStringToInt(str1);
		fos.write(c);
		fos.flush();
		
		//第二次读文件--第一次是得到了字符以及对应的次数
		//第二次是对文件内容进行编码
		int value=fis.read();
		String str="";
		//读文件，并将对应的哈夫曼编码拼接成字符串
		while(value!=-1) {
			str+=HuffmCodes[value];
			value=fis.read();
		}
		System.out.println(str);
		fis.close();
		//下面每8个一组，进行写入，不够后面补0
		String s="";
		while(str.length()>=8) {
			s=str.substring(0,8);
			int b=changeStringToInt(s);
			fos.write(b);
			fos.flush();
			str=str.substring(8);
		}
		//不足8的进行补0操作
		int last1=8-str.length();
		for(int i=0;i<last1;i++) {
			str+="0";
		}
		s=str.substring(0,8);
		int d=changeStringToInt(s);
		fos.write(d);
		fos.close();
	}
	 
	//得到要插入元素正确的索引
	public int getIndex(HuffmNode node) {
		for (int i = 0; i < list.size(); i++) {
			if(node.getData()<=list.get(i).getData()){
				return i;
			}
		}
       return list.size();
	}

	//将字符串转换成整数
	//将8为的额串转换成十进制进行储存
	public int changeStringToInt(String s){
		int v1=(s.charAt(0)-48)*128;
		int v2=(s.charAt(1)-48)*64;
		int v3=(s.charAt(2)-48)*32;
		int v4=(s.charAt(3)-48)*16;
		int v5=(s.charAt(4)-48)*8;
		int v6=(s.charAt(5)-48)*4;
		int v7=(s.charAt(6)-48)*2;
		int v8=(s.charAt(7)-48)*1;
		return v1+v2+v3+v4+v5+v6+v7+v8;
			
	}
}
