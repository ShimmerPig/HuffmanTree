package HuffmCompress;

import java.io.FileInputStream;
import java.io.FileOutputStream;

//解压缩
public class Decompress {
	//存储每个哈夫曼编码的长度
	public int [] codelengths=new int[256];
	//得到对应的哈夫曼编码值--解码的小本本
	public String [] codeMap=new String[256];
	
	public static void main(String[]args) {
		Decompress d=new Decompress();
		d.decompress("C:\\Users\\lenovo\\Desktop\\testresult.zip",
				"C:\\Users\\lenovo\\Desktop\\test2.txt");
	}
	
	//解压方法
	public void decompress(String srcpath,String destpath) {
		try {
			FileInputStream fis=new FileInputStream(srcpath);
			FileOutputStream fos=new FileOutputStream(destpath);
			int value;
			int codeLength=0;
			String code="";
			//还原码表
			for(int i=0;i<codelengths.length;i++) {
				//读出码表中的编码长度
				value=fis.read();
				codelengths[i]=value;
				//对每个编码的长度累加
				codeLength+=codelengths[i];
			}
			
			//得到编码的总长度
			//将总长度除以8得到字节个数
			//十进制的个数
			int len=codeLength/8;
			//如果不是8的倍数，则字节个数加1（对应压缩补0的情况）
			if((codeLength)%8!=0) {
				len++;
			}
			
			//读取哈夫曼编码
			//len是整数的个数
			for(int i=0;i<len;i++) {
				//把读到的整数转换成二进制
				//对哈夫曼编码进行累加
				code+=changeIntToString(fis.read());
			}
			//将每个字符对应的哈夫曼编码储存到codeMap中
			for(int i=0;i<codeMap.length;i++) {
				//如果第i个位置编码的长度不为0，说明这个位置有哈夫曼编码
				//将一大串编码按照这个位置的编码的长度划分，
				//结果存储到这个位置的codeMap中
				if(codelengths[i]!=0) {
					String ss=code.substring(0,codelengths[i]);
					codeMap[i]=ss;
					code=code.substring(codelengths[i]);
				}else {
					//这个位置没有对应的哈夫曼编码
					codeMap[i]="";
				}
			}
			
			//读取压缩的文件内容
			String codeContent="";
			//available获取流的大小
			while(fis.available()>1) {
				//将读到的十进制的压缩后的十进制文件内容，转换成二进制的哈夫曼编码
				codeContent+=changeIntToString(fis.read());
			}
			//读取最后一个
			value=fis.read();
			codeContent+=changeIntToString(value);
//			System.out.println(value);
//			System.out.println(changeIntToString(value));
			/*这里有bug*/
			//把最后补的0给去掉
			codeContent=codeContent.substring(0, codeContent.length()-6);
			//对文件中的哈夫曼编码的一大串内容进行截取后，
			//与map数组中的字符的哈夫曼编码进行比较，从而解码
			for(int i=0;i<codeContent.length();i++) {
				String codecontent=codeContent.substring(0,i+1);
				for(int j=0;j<codeMap.length;j++) {
					if(codeMap[j].equals(codecontent)) {
						//这里写出的是这个字符在码表中的下标
						fos.write(j);
						fos.flush();
						codeContent=codeContent.substring(i+1);
						//保证了下次截取又是从0开始
						i=-1;
						break;
					}
				}
			}
			
			fos.close();
			fis.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//十进制转二进制字符串
	public String changeIntToString(int value) {
		String s="";
		for (int i = 0; i < 8; i++) {
			s=value%2+s;
			value=value/2;
		}
		return s;
	}

}
