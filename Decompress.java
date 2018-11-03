package HuffmCompress;

import java.io.FileInputStream;
import java.io.FileOutputStream;

//��ѹ��
public class Decompress {
	//�洢ÿ������������ĳ���
	public int [] codelengths=new int[256];
	//�õ���Ӧ�Ĺ���������ֵ--�����С����
	public String [] codeMap=new String[256];
	
	public static void main(String[]args) {
		Decompress d=new Decompress();
		d.decompress("C:\\Users\\lenovo\\Desktop\\testresult.zip",
				"C:\\Users\\lenovo\\Desktop\\test2.txt");
	}
	
	//��ѹ����
	public void decompress(String srcpath,String destpath) {
		try {
			FileInputStream fis=new FileInputStream(srcpath);
			FileOutputStream fos=new FileOutputStream(destpath);
			int value;
			int codeLength=0;
			String code="";
			//��ԭ���
			for(int i=0;i<codelengths.length;i++) {
				//��������еı��볤��
				value=fis.read();
				codelengths[i]=value;
				//��ÿ������ĳ����ۼ�
				codeLength+=codelengths[i];
			}
			
			//�õ�������ܳ���
			//���ܳ��ȳ���8�õ��ֽڸ���
			//ʮ���Ƶĸ���
			int len=codeLength/8;
			//�������8�ı��������ֽڸ�����1����Ӧѹ����0�������
			if((codeLength)%8!=0) {
				len++;
			}
			
			//��ȡ����������
			//len�������ĸ���
			for(int i=0;i<len;i++) {
				//�Ѷ���������ת���ɶ�����
				//�Թ�������������ۼ�
				code+=changeIntToString(fis.read());
			}
			//��ÿ���ַ���Ӧ�Ĺ��������봢�浽codeMap��
			for(int i=0;i<codeMap.length;i++) {
				//�����i��λ�ñ���ĳ��Ȳ�Ϊ0��˵�����λ���й���������
				//��һ�󴮱��밴�����λ�õı���ĳ��Ȼ��֣�
				//����洢�����λ�õ�codeMap��
				if(codelengths[i]!=0) {
					String ss=code.substring(0,codelengths[i]);
					codeMap[i]=ss;
					code=code.substring(codelengths[i]);
				}else {
					//���λ��û�ж�Ӧ�Ĺ���������
					codeMap[i]="";
				}
			}
			
			//��ȡѹ�����ļ�����
			String codeContent="";
			//available��ȡ���Ĵ�С
			while(fis.available()>1) {
				//��������ʮ���Ƶ�ѹ�����ʮ�����ļ����ݣ�ת���ɶ����ƵĹ���������
				codeContent+=changeIntToString(fis.read());
			}
			//��ȡ���һ��
			value=fis.read();
			codeContent+=changeIntToString(value);
//			System.out.println(value);
//			System.out.println(changeIntToString(value));
			/*������bug*/
			//����󲹵�0��ȥ��
			codeContent=codeContent.substring(0, codeContent.length()-6);
			//���ļ��еĹ����������һ�����ݽ��н�ȡ��
			//��map�����е��ַ��Ĺ�����������бȽϣ��Ӷ�����
			for(int i=0;i<codeContent.length();i++) {
				String codecontent=codeContent.substring(0,i+1);
				for(int j=0;j<codeMap.length;j++) {
					if(codeMap[j].equals(codecontent)) {
						//����д����������ַ�������е��±�
						fos.write(j);
						fos.flush();
						codeContent=codeContent.substring(i+1);
						//��֤���´ν�ȡ���Ǵ�0��ʼ
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
	//ʮ����ת�������ַ���
	public String changeIntToString(int value) {
		String s="";
		for (int i = 0; i < 8; i++) {
			s=value%2+s;
			value=value/2;
		}
		return s;
	}

}
