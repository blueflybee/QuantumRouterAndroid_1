/*
 * Copyright 2018 Google Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.android.sambadocumentsprovider.mount;

import android.os.Environment;
import android.widget.LinearLayout;

public class Const
{
  public static String path= Environment.getExternalStorageDirectory().getPath()+"/wenjian/";                                               //�����Ƭ��ַ

  public static final String[][] MIME_MapTable={
	  {"3gp",   "video/3gpp"},    {"pdb", "chemical/x-pdb"},
		{"aab",   "application/x-authoware-bin"},    {"pdf", "application/pdf"}, 
		{"aam",   "application/x-authoware-map"},    {"pfr", "application/font-tdpfr"}, 
		{"aas",   "application/x-authoware-seg"},    {"pgm", "image/x-portable-graymap"}, 
		{"ai",   "application/postscript"},    {"pict", "image/x-pict"}, 
		{"aif",   "audio/x-aiff"},    {"pm", "application/x-perl"}, 
		{"aifc",   "audio/x-aiff"},    {"pmd", "application/x-pmd"}, 
		{"aiff",   "audio/x-aiff"},    {"png", "image/png"}, 
		{"als",   "audio/X-Alpha5"},    {"pnm", "image/x-portable-anymap"}, 
		{"amc",   "application/x-mpeg"},    {"pnz", "image/png"}, 
		{"ani",   "application/octet-stream"},    {"pot", "application/vnd.ms-powerpoint"}, 
		{"apk",   "application/vnd.android.package-archive"},    {"ppm", "image/x-portable-pixmap"}, 
		{"asc",   "text/plain"},    {"pps", "application/vnd.ms-powerpoint"}, 
		{"asd",   "application/astound"},    {"ppt", "application/vnd.ms-powerpoint"}, 
		{"pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, 
		{"asf",   "video/x-ms-asf"},    {"pqf", "application/x-cprplayer"}, 
		{"asn",   "application/astound"},    {"pqi", "application/cprplayer"}, 
		{"asp",   "application/x-asap"},    {"prc", "application/x-prc"}, 
		{"asx",   "video/x-ms-asf"},    {"proxy", "application/x-ns-proxy-autoconfig"}, 
		{"au",   "audio/basic"},    {"ps", "application/postscript"}, 
		{"avb",   "application/octet-stream"},    {"ptlk", "application/listenup"}, 
		{"avi",   "video/x-msvideo"},    {"pub", "application/x-mspublisher"}, 
		{"awb",   "audio/amr-wb"},    {"pvx", "video/x-pv-pvx"}, 
		{"bcpio",   "application/x-bcpio"},    {"qcp", "audio/vnd.qcelp"}, 
		{"bin",   "application/octet-stream"},    {"qt", "video/quicktime"}, 
		{"bld",   "application/bld"},    {"qti", "image/x-quicktime"}, 
		{"bld2",   "application/bld2"},    {"qtif", "image/x-quicktime"}, 
		{"bmp",   "image/bmp"},    {"r3t", "text/vnd.rn-realtext3d"}, 
		{"bpk",   "application/octet-stream"},    {"ra", "audio/x-pn-realaudio"}, 
		{"bz2",   "application/x-bzip2"},    {"ram", "audio/x-pn-realaudio"}, 
		{"cal",   "image/x-cals"},    {"rar", "application/x-rar-compressed"}, 
		{"ccn",   "application/x-cnc"},    {"ras", "image/x-cmu-raster"}, 
		{"cco",   "application/x-cocoa"},    {"rdf", "application/rdf+xml"}, 
		{"cdf",   "application/x-netcdf"},    {"rf", "image/vnd.rn-realflash"}, 
		{"cgi",   "magnus-internal/cgi"},    {"rgb", "image/x-rgb"}, 
		{"chat",   "application/x-chat"},    {"rlf", "application/x-richlink"}, 
		{"class",   "application/octet-stream"},    {"rm", "audio/x-pn-realaudio"}, 
		{"clp",   "application/x-msclip"},    {"rmf", "audio/x-rmf"}, 
		{"cmx",   "application/x-cmx"},    {"rmm", "audio/x-pn-realaudio"}, 
		{"co",   "application/x-cult3d-object"},    {"rmvb", "audio/x-pn-realaudio"}, 
		{"cod",   "image/cis-cod"},    {"rnx", "application/vnd.rn-realplayer"}, 
		{"cpio",   "application/x-cpio"},    {"roff", "application/x-troff"}, 
		{"cpt",   "application/mac-compactpro"},    {"rp", "image/vnd.rn-realpix"}, 
		{"crd",   "application/x-mscardfile"},    {"rpm", "audio/x-pn-realaudio-plugin"}, 
		{"csh",   "application/x-csh"},    {"rt", "text/vnd.rn-realtext"}, 
		{"csm",   "chemical/x-csml"},    {"rte", "x-lml/x-gps"}, 
		{"csml",   "chemical/x-csml"},    {"rtf", "application/rtf"}, 
		{"css",   "text/css"},    {"rtg", "application/metastream"}, 
		{"cur",   "application/octet-stream"},    {"rtx", "text/richtext"}, 
		{"dcm",   "x-lml/x-evm"},    {"rv", "video/vnd.rn-realvideo"}, 
		{"dcr",   "application/x-director"},    {"rwc", "application/x-rogerwilco"}, 
		{"dcx",   "image/x-dcx"},    {"s3m", "audio/x-mod"}, 
		{"dhtml",   "text/html"},    {"s3z", "audio/x-mod"}, 
		{"dir",   "application/x-director"},    {"sca", "application/x-supercard"}, 
		{"dll",   "application/octet-stream"},    {"scd", "application/x-msschedule"}, 
		{"dmg",   "application/octet-stream"},    {"sdf", "application/e-score"}, 
		{"dms",   "application/octet-stream"},    {"sea", "application/x-stuffit"}, 
		{"doc",   "application/msword"},    {"sgm", "text/x-sgml"}, 
		{"docx",  "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
		{"dot",   "application/x-dot"},    {"sgml", "text/x-sgml"}, 
		{"dvi",   "application/x-dvi"},    {"sh", "application/x-sh"}, 
		{"dwf",   "drawing/x-dwf"},    {"shar", "application/x-shar"}, 
		{"dwg",   "application/x-autocad"},    {"shtml", "magnus-internal/parsed-html"}, 
		{"dxf",   "application/x-autocad"},    {"shw", "application/presentations"}, 
		{"dxr",   "application/x-director"},    {"si6", "image/si6"}, 
		{"ebk",   "application/x-expandedbook"},    {"si7", "image/vnd.stiwap.sis"}, 
		{"emb",   "chemical/x-embl-dl-nucleotide"},    {"si9", "image/vnd.lgtwap.sis"}, 
		{"embl",   "chemical/x-embl-dl-nucleotide"},    {"sis", "application/vnd.symbian.install"}, 
		{"eps",   "application/postscript"},    {"sit", "application/x-stuffit"}, 
		{"eri",   "image/x-eri"},    {"skd", "application/x-Koan"}, 
		{"es",   "audio/echospeech"},    {"skm", "application/x-Koan"}, 
		{"esl",   "audio/echospeech"},    {"skp", "application/x-Koan"}, 
		{"etc",   "application/x-earthtime"},    {"skt", "application/x-Koan"}, 
		{"etx",   "text/x-setext"},    {"slc", "application/x-salsa"}, 
		{"evm",   "x-lml/x-evm"},    {"smd", "audio/x-smd"}, 
		{"evy",   "application/x-envoy"},    {"smi", "application/smil"}, 
		{"exe",   "application/octet-stream"},    {"smil", "application/smil"}, 
		{"fh4",   "image/x-freehand"},    {"smp", "application/studiom"}, 
		{"fh5",   "image/x-freehand"},    {"smz", "audio/x-smd"}, 
		{"fhc",   "image/x-freehand"},    {"snd", "audio/basic"}, 
		{"fif",   "image/fif"},    {"spc", "text/x-speech"}, 
		{"fm",   "application/x-maker"},    {"spl", "application/futuresplash"}, 
		{"fpx",   "image/x-fpx"},    {"spr", "application/x-sprite"}, 
		{"fvi",   "video/isivideo"},    {"sprite", "application/x-sprite"}, 
		{"gau",   "chemical/x-gaussian-input"},    {"spt", "application/x-spt"}, 
		{"gca",   "application/x-gca-compressed"},    {"src", "application/x-wais-source"}, 
		{"gdb",   "x-lml/x-gdb"},    {"stk", "application/hyperstudio"}, 
		{"gif",   "image/gif"},    {"stm", "audio/x-mod"}, 
		{"gps",   "application/x-gps"},    {"sv4cpio", "application/x-sv4cpio"}, 
		{"gtar",   "application/x-gtar"},    {"sv4crc", "application/x-sv4crc"}, 
		{"gz",   "application/x-gzip"},    {"svf", "image/vnd"}, 
		{"hdf",   "application/x-hdf"},    {"svg", "image/svg-xml"}, 
		{"hdm",   "text/x-hdml"},    {"svh", "image/svh"}, 
		{"hdml",   "text/x-hdml"},    {"svr", "x-world/x-svr"}, 
		{"hlp",   "application/winhlp"},    {"swf", "application/x-shockwave-flash"}, 
		{"hqx",   "application/mac-binhex40"},    {"swfl", "application/x-shockwave-flash"}, 
		{"htm",   "text/html"},    {"t", "application/x-troff"}, 
		{"html",   "text/html"},    {"tad", "application/octet-stream"}, 
		{"hts",   "text/html"},    {"talk", "text/x-speech"}, 
		{"ice",   "x-conference/x-cooltalk"},    {"tar", "application/x-tar"}, 
		{"ico",   "application/octet-stream"},    {"taz", "application/x-tar"}, 
		{"ief",   "image/ief"},    {"tbp", "application/x-timbuktu"}, 
		{"ifm",   "image/gif"},    {"tbt", "application/x-timbuktu"}, 
		{"ifs",   "image/ifs"},    {"tcl", "application/x-tcl"}, 
		{"imy",   "audio/melody"},    {"tex", "application/x-tex"}, 
		{"ins",   "application/x-NET-Install"},    {"texi", "application/x-texinfo"}, 
		{"ips",   "application/x-ipscript"},    {"texinfo", "application/x-texinfo"}, 
		{"ipx",   "application/x-ipix"},    {"tgz", "application/x-tar"}, 
		{"it",   "audio/x-mod"},    {"thm", "application/vnd.eri.thm"}, 
		{"itz",   "audio/x-mod"},    {"tif", "image/tiff"}, 
		{"ivr",   "i-world/i-vrml"},    {"tiff", "image/tiff"}, 
		{"j2k",   "image/j2k"},    {"tki", "application/x-tkined"}, 
		{"jad",   "text/vnd.sun.j2me.app-descriptor"},    {"tkined", "application/x-tkined"}, 
		{"jam",   "application/x-jam"},    {"toc", "application/toc"}, 
		{"jar",   "application/java-archive"},    {"toy", "image/toy"}, 
		{"jnlp",   "application/x-java-jnlp-file"},    {"tr", "application/x-troff"}, 
		{"jpe",   "image/jpeg"},    {"trk", "x-lml/x-gps"}, 
		{"jpeg",   "image/jpeg"},    {"trm", "application/x-msterminal"}, 
		{"jpg",   "image/jpeg"},    {"tsi", "audio/tsplayer"}, 
		{"jpz",   "image/jpeg"},    {"tsp", "application/dsptype"}, 
		{"js",   "application/x-javascript"},    {"tsv", "text/tab-separated-values"}, 
		{"jwc",   "application/jwc"},    {"tsv", "text/tab-separated-values"}, 
		{"kjx",   "application/x-kjx"},    {"ttf", "application/octet-stream"}, 
		{"lak",   "x-lml/x-lak"},    {"ttz", "application/t-time"}, 
		{"latex",   "application/x-latex"},    {"txt", "text/plain"}, 
		{"lcc",   "application/fastman"},    {"ult", "audio/x-mod"}, 
		{"lcl",   "application/x-digitalloca"},    {"ustar", "application/x-ustar"}, 
		{"lcr",   "application/x-digitalloca"},    {"uu", "application/x-uuencode"}, 
		{"lgh",   "application/lgh"},    {"uue", "application/x-uuencode"}, 
		{"lha",   "application/octet-stream"},    {"vcd", "application/x-cdlink"}, 
		{"lml",   "x-lml/x-lml"},    {"vcf", "text/x-vcard"}, 
		{"lmlpack",   "x-lml/x-lmlpack"},    {"vdo", "video/vdo"}, 
		{"lsf",   "video/x-ms-asf"},    {"vib", "audio/vib"}, 
		{"lsx",   "video/x-ms-asf"},    {"viv", "video/vivo"}, 
		{"lzh",   "application/x-lzh"},    {"vivo", "video/vivo"}, 
		{"m13",   "application/x-msmediaview"},    {"vmd", "application/vocaltec-media-desc"}, 
		{"m14",   "application/x-msmediaview"},    {"vmf", "application/vocaltec-media-file"}, 
		{"m15",   "audio/x-mod"},    {"vmi", "application/x-dreamcast-vms-info"}, 
		{"m3u",   "audio/x-mpegurl"},    {"vms", "application/x-dreamcast-vms"}, 
		{"m3url",   "audio/x-mpegurl"},    {"vox", "audio/voxware"}, 
		{"ma1",   "audio/ma1"},    {"vqe", "audio/x-twinvq-plugin"}, 
		{"ma2",   "audio/ma2"},    {"vqf", "audio/x-twinvq"}, 
		{"ma3",   "audio/ma3"},    {"vql", "audio/x-twinvq"}, 
		{"ma5",   "audio/ma5"},    {"vre", "x-world/x-vream"}, 
		{"man",   "application/x-troff-man"},    {"vrml", "x-world/x-vrml"}, 
		{"map",   "magnus-internal/imagemap"},    {"vrt", "x-world/x-vrt"}, 
		{"mbd",   "application/mbedlet"},    {"vrw", "x-world/x-vream"}, 
		{"mct",   "application/x-mascot"},    {"vts", "workbook/formulaone"}, 
		{"mdb",   "application/x-msaccess"},    {"wav", "audio/x-wav"}, 
		{"mdz",   "audio/x-mod"},    {"wax", "audio/x-ms-wax"}, 
		{"me",   "application/x-troff-me"},    {"wbmp", "image/vnd.wap.wbmp"}, 
		{"mel",   "text/x-vmel"},    {"web", "application/vnd.xara"}, 
		{"mi",   "application/x-mif"},    {"wi", "image/wavelet"}, 
		{"mid",   "audio/midi"},    {"wis", "application/x-InstallShield"}, 
		{"midi",   "audio/midi"},    {"wm", "video/x-ms-wm"}, 
		{"mif",   "application/x-mif"},    {"wma", "audio/x-ms-wma"}, 
		{"mil",   "image/x-cals"},    {"wmd", "application/x-ms-wmd"}, 
		{"mio",   "audio/x-mio"},    {"wmf", "application/x-msmetafile"}, 
		{"mmf",   "application/x-skt-lbs"},    {"wml", "text/vnd.wap.wml"}, 
		{"mng",   "video/x-mng"},    {"wmlc", "application/vnd.wap.wmlc"}, 
		{"mny",   "application/x-msmoney"},    {"wmls", "text/vnd.wap.wmlscript"}, 
		{"moc",   "application/x-mocha"},    {"wmlsc", "application/vnd.wap.wmlscriptc"}, 
		{"mocha",   "application/x-mocha"},    {"wmlscript", "text/vnd.wap.wmlscript"}, 
		{"mod",   "audio/x-mod"},    {"wmv", "audio/x-ms-wmv"}, 
		{"mof",   "application/x-yumekara"},    {"wmx", "video/x-ms-wmx"}, 
		{"mol",   "chemical/x-mdl-molfile"},    {"wmz", "application/x-ms-wmz"}, 
		{"mop",   "chemical/x-mopac-input"},    {"wpng", "image/x-up-wpng"}, 
		{"mov",   "video/quicktime"},    {"wpt", "x-lml/x-gps"}, 
		{"movie",   "video/x-sgi-movie"},    {"wri", "application/x-mswrite"}, 
		{"mp2",   "audio/x-mpeg"},    {"wrl", "x-world/x-vrml"}, 
		{"mp3",   "audio/x-mpeg"},    {"wrz", "x-world/x-vrml"}, 
		{"mp4",   "video/mp4"},    {"ws", "text/vnd.wap.wmlscript"}, 
		{"mpc",   "application/vnd.mpohun.certificate"},    {"wsc", "application/vnd.wap.wmlscriptc"}, 
		{"mpe",   "video/mpeg"},    {"wv", "video/wavelet"}, 
		{"mpeg",   "video/mpeg"},    {"wvx", "video/x-ms-wvx"}, 
		{"mpg",   "video/mpeg"},    {"wxl", "application/x-wxl"}, 
		{"mpg4",   "video/mp4"},    {"x-gzip", "application/x-gzip"}, 
		{"mpga",   "audio/mpeg"},    {"xar", "application/vnd.xara"}, 
		{"mpn",   "application/vnd.mophun.application"},    {"xbm", "image/x-xbitmap"}, 
		{"mpp",   "application/vnd.ms-project"},    {"xdm", "application/x-xdma"}, 
		{"mps",   "application/x-mapserver"},    {"xdma", "application/x-xdma"}, 
		{"mrl",   "text/x-mrml"},    {"xdw", "application/vnd.fujixerox.docuworks"}, 
		{"mrm",   "application/x-mrm"},    {"xht", "application/xhtml+xml"}, 
		{"ms",   "application/x-troff-ms"},    {"xhtm", "application/xhtml+xml"}, 
		{"mts",   "application/metastream"},    {"xhtml", "application/xhtml+xml"}, 
		{"mtx",   "application/metastream"},    {"xla", "application/vnd.ms-excel"}, 
		{"mtz",   "application/metastream"},    {"xlc", "application/vnd.ms-excel"}, 
		{"mzv",   "application/metastream"},    {"xll", "application/x-excel"}, 
		{"nar",   "application/zip"},    {"xlm", "application/vnd.ms-excel"}, 
		{"nbmp",   "image/nbmp"},    {"xls", "application/vnd.ms-excel"}, 
		{"xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
		{"nc",   "application/x-netcdf"},    {"xlt", "application/vnd.ms-excel"}, 
		{"ndb",   "x-lml/x-ndb"},    {"xlw", "application/vnd.ms-excel"}, 
		{"ndwn",   "application/ndwn"},    {"xm", "audio/x-mod"}, 
		{"nif",   "application/x-nif"},    {"xml", "text/xml"}, 
		{"nmz",   "application/x-scream"},    {"xmz", "audio/x-mod"}, 
		{"nokia-op-logo",   "image/vnd.nok-oplogo-color"},    {"xpi", "application/x-xpinstall"}, 
		{"npx",   "application/x-netfpx"},    {"xpm", "image/x-xpixmap"}, 
		{"nsnd",   "audio/nsnd"},    {"xsit", "text/xml"}, 
		{"nva",   "application/x-neva1"},    {"xsl", "text/xml"}, 
		{"oda",   "application/oda"},    {"xul", "text/xul"}, 
		{"oom",   "application/x-AtlasMate-Plugin"},    {"xwd", "image/x-xwindowdump"}, 
		{"pac",   "audio/x-pac"},    {"xyz", "chemical/x-pdb"}, 
		{"pae",   "audio/x-epac"},    {"yz1", "application/x-yz1"}, 
		{"pan",   "application/x-pan"},    {"z", "application/x-compress"}, 
		{"pbm",   "image/x-portable-bitmap"},    {"zac", "application/x-zaurus-zac"}, 
		{"pcx",   "image/x-pcx"},    {"zip", "application/zip"}, 
		{"pda",   "image/x-pda"},    {"", "*/*"}, 
 
      }; 
  
  public static final int EXPIRE_DURATION=1;             //�������µ�ʱ������1Сʱ��
  public static final int KILL_PROCESS=2;                //�˳�����
  public static final int BACK_TO_HOME=3;                //������ҳ
  public static final int ANSWER_TO_HELP=10;             //�ش������ⷵ��������ҳ
  public static final int FP= LinearLayout.LayoutParams.FILL_PARENT;
  public static final int WC= LinearLayout.LayoutParams.WRAP_CONTENT;

  public static class SringID
  {
    public static final String Home         ="Home";
    public static final String Login        ="Login";
    public static final String Regist 	    ="Regist";
    public static final String GEJU   ="GEJU";
    public static final String SHIERGONG 		="SHIERGONG";
    public static final String YUN 		="YUN";
    public static final String SHIJIANTUICE 		="SHIJIANTUICE";
    public static final String New          ="New";
  }

  public static class RequstCode
  {
    public static final int LOGIN_FOR_PAY           = 1002; //Ϊ�����Ѷ����еĵ�¼
    public static final int LOGIN                   = 1003; //��¼
    public static final int HOME                    = 1004; //
    public static final int QUESTION                = 1005; //������Ϣ
    public static final int SCHOLLWORK              = 1006; //��ҵ��Ϣ
    public static final int SCHOLLWORKADD           = 1007; //��ҵ��Ϣ
    public static final int CHANGE_USER             = 1008; //����û�
    public static final int WORKLIST                = 1009; //�γ�ǩ��
    public static final int ABOUT                   = 1010; //����
    public static final int ADVISE                  = 1011; //���鷴��
    public static final int CITY                    = 1012; //��������
    public static final int VISITOR                 = 1013; //�����¼�Ĺ���
    public static final int DEVICEINSPECTION        = 1014; //Ѳ���б�
    public static final int INSPECTION              = 1015; //Ѳ��
    public static final int INSPECTIONDETAILS       = 1016; //Ѳ���¼
    public static final int SETTING                 = 1017; //����
  }

  public static class FlowNo
  {
    public static final int MAIN                    = 0; //������
    public static final int LOGIN                   = 1; //��¼
    public static final int HOME                    = 2; //��ҳ
    public static final int INFO                    = 3; //������Ϣ
    public static final int ADVISE          = 4; //���鷴��
    
    public static final int WORKMAIN          		= 10; //Ѳ��ƻ�
    public static final int RESOURCE		        = 11; //Ѳ����Դ
    public static final int QIANDAO		            = 12; //Ѳ��ǩ��
    public static final int TASKSET		        	= 13; //��ҵ��д
    
    public static final int SCHOOLWORK		       	= 14; //��ҵ����
    public static final int SCHOOLWORKAdd		    = 15; //������ҵ
    
    public static final int HELP		        	= 16; //�����
    public static final int QUESTION			    = 17; //������Ϣ
    public static final int QUESTIONADD			    = 18; //������
    public static final int ANSWER			    	= 19; //�ظ���Ϣ
    
    public static final int SETTING                 =20; //����
    public static final int SETTING_USER            =21; //��Ա����   
    public static final int SETTING_ABOUT           =23; //��Ա����
    
    public static final int DAO						=30;//ǩ��
    public static final int DAORECORD 			    =31;//ǩ����¼

  }

  public static class ConnectState
  {
    public static final int DISCONNECT         =0;            //δ��¼
    public static final int GUEST              =1;            //�ο�
    public static final int CUSTOMER           =2;            //�û�
  }
  //��Կ����
  public static final String SEED             ="dkflsake";

}
