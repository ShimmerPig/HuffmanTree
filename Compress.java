package HuffmCompress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.GeneralSecurityException;
import java.util.LinkedList;

import javax.swing.text.ChangedCharSetException;

//ѹ���Լ�ѹ����ʵ����
public class Compress {
	//�洢���ִ���������
	public int [] times=new int [256];
	//string���͵����飬�洢ÿ��Ҷ�ӽڵ�Ĺ���������
	public String [] HuffmCodes=new String[256];
	//�����洢����list
	public LinkedList<HuffmNode>list=new LinkedList<>();
	
	//��ÿ��Ҷ�ӽڵ�ı�����г�ʼ��
	public Compress() {
		for(int i=0;i<HuffmCodes.length;i++) {
			HuffmCodes[i]="";
		}
	}
	
	//ͳ��ÿ���ַ����ִ���
	//pathΪҪͳ�Ƶ��ļ����ڵ�·��
	public void countTimes(String path)throws Exception{
		//�ļ�������
		FileInputStream fis=new FileInputStream(path);
		//�ļ��е����ַ�������õ���������ַ���Ӧ��ascii�룬��������Ϊ�����е��±�
		int value=fis.read();
		while(value!=-1) {
			times[value]++;
			value=fis.read();
		}
		fis.close();
	}
	
	//�����������  ���ظ��ڵ�
	public HuffmNode createTree() {
		for(int i=0;i<times.length;i++) {
			if(times[i]!=0) {
				HuffmNode node=new HuffmNode(times[i], i);
				//����list�е���ȷλ��--������һ����С���������list
				list.add(getIndex(node),node);
			}
		}
		//�����������
		while(list.size()>1) {
			HuffmNode firstNode=list.removeFirst();
			HuffmNode secondNode=list.removeFirst();
			//��������Ϊ-1
			HuffmNode father=new HuffmNode(firstNode.getData()+secondNode.getData(), -1);
			father.setLeft(firstNode);
			father.setRight(secondNode);
			list.add(getIndex(father),father);
		}
		return list.getFirst();
	}
	
	//ǰ�������ȡ�����Ǳ���
	public void getHuffmCode(HuffmNode root,String code) {
		if(root.getLeft()!=null) {
			getHuffmCode(root.getLeft(), code+"0");
		}
		if(root.getRight()!=null) {
			getHuffmCode(root.getRight(), code+"1");
		}
		//Ҷ�ӽڵ�--�����Ҷ�ӽڵ��е�index��һ����ȷ��  ����Ҫ��������Ľ�㣬ֻ��Ҷ�ӽڵ�Ļᱻ�����codes����
		if(root.getLeft()==null&&root.getRight()==null) {
			HuffmCodes[root.getIndex()]=code;
		}
	}
	
	//ѹ���ļ�
	//ѹ��path�е��ļ�����ѹ������ļ�output��destpath��
	public void compress(String path,String destpath)throws Exception{
		FileOutputStream fos=new FileOutputStream(destpath);
		FileInputStream fis=new FileInputStream(path);
		
		//�����������������Լ�ÿ������ĳ���д���ļ���
		//��Ϊ��ѹʱ������뱾
		String code="";
		for(int i=0;i<256;i++) {
			//����ֻ�ǽ��̶�д��
			//���ҶԱ�������ۼ�
			fos.write(HuffmCodes[i].length());
			code+=HuffmCodes[i];
			fos.flush();
		}
		
		//���ۼӺ�Ĺ���������д���ļ��У�����8��һ����ν���ת����10���ƺ�д�룩������8����0
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
		
		//�ڶ��ζ��ļ�--��һ���ǵõ����ַ��Լ���Ӧ�Ĵ���
		//�ڶ����Ƕ��ļ����ݽ��б���
		int value=fis.read();
		String str="";
		//���ļ���������Ӧ�Ĺ���������ƴ�ӳ��ַ���
		while(value!=-1) {
			str+=HuffmCodes[value];
			value=fis.read();
		}
		System.out.println(str);
		fis.close();
		//����ÿ8��һ�飬����д�룬�������油0
		String s="";
		while(str.length()>=8) {
			s=str.substring(0,8);
			int b=changeStringToInt(s);
			fos.write(b);
			fos.flush();
			str=str.substring(8);
		}
		//����8�Ľ��в�0����
		int last1=8-str.length();
		for(int i=0;i<last1;i++) {
			str+="0";
		}
		s=str.substring(0,8);
		int d=changeStringToInt(s);
		fos.write(d);
		fos.close();
	}
	 
	//�õ�Ҫ����Ԫ����ȷ������
	public int getIndex(HuffmNode node) {
		for (int i = 0; i < list.size(); i++) {
			if(node.getData()<=list.get(i).getData()){
				return i;
			}
		}
       return list.size();
	}

	//���ַ���ת��������
	//��8Ϊ�Ķת����ʮ���ƽ��д���
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
