class Syn{
	public Syn(String name, String pre, String post, String type, String imgNum) {
		synID=name;
		this.imgNum=imgNum;
		this.pre=pre;
		this.lastpre=pre;
		this.post=post;
		String[] posts = post.split(",");
		if (posts.length>=1) post1=posts[0];
		if (posts.length>=2) post2=posts[1];
		if (posts.length>=3) post3=posts[2];
		if (posts.length==4) post4=posts[3];
		this.type=type;
		members=name;
		continNum=0;

	}
	void setType2(String type2)
	{
		this.type2=type2;
	}
	
	void setContinNum(int conN)
	{
		this.continNum=conN;
	}
	
	void setSeries(String series)
	{
		this.series=series;
	}
	
	void addMember(String name)
	{
		members=members+","+name;
		
	}
	public void setSynID(String id){
		synID=id;
	}
	public void setPrename(String name){
		prename=name;
	}

	public void setSections(int sections){
		this.sections=sections;
	}
	public void setMid(String mid){
		this.mid=mid;
	}
	public void setLastmember(String lmem){
		this.lastmember=lmem;
	}
	public void setFirstmember(String fmem){
		this.firstmember=fmem;
	}
	
	
	public void setLastImage(String lmem){
		this.lastImage=lmem;
	}
	public void setFirstImage(String fmem){
		this.firstImage=fmem;
	}
	
	public void setPostlength(int postlength){
		this.postlength=postlength;
	}
	
	public void setLastpre(String lastpre){
		this.lastpre=lastpre;
	}
	
	public void setPostnames(String postname)
	{
		this.postname=postname;
		//Util.info("postname: "+postname);
		String[] postnames = postname.split(",");
		partnerNum = postnames.length;
		if (postnames.length>=1) postname1=postnames[0];
		if (postnames.length>=2) postname2=postnames[1];
		if (postnames.length>=3) postname3=postnames[2];
		if (postnames.length==4) postname4=postnames[3];
		setPostlength(postnames.length);
		
	}
	public void setFirstPostnames(String postname)
	{
		this.firstpostname=postname;
		
	}
	public void setLastPostnames(String postname)
	{
		this.lastpostname=postname;
		
	}
	
	public void shift()
	{
		if (type.equals("electrical")) 
		{
			if (prename.compareTo(postname)<0) 
			{
				
				String s = pre;
				pre = post;
				post = s;
				
				s = prename;
				prename = postname;
				postname = s;
				
			}
		}
	}
	
	
	
	
	
	String synID="";
	String type="";
	String pre="";
	String post="";
	String post1="",post2="",post3="",post4="";
	String postname1="",postname2="",postname3="",postname4="";
	String members="";
	String prename;
	String postname;
	int sections;
	int postlength;
	String lastpre;
	String imgNum;
	String series;
	String type2;
	int partnerNum;
	String mid;
	String lastmember;
	String firstmember;
	String lastImage;
	String firstImage;
	String firstpostname;
	String lastpostname;
	int continNum;

}
