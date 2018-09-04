package id.code.avrist.plugin;

public class Globals {
  private static Globals instance;

  private Object data;

  private Globals(){}

  public void setData(Object d){
    this.data=d;
  }
  public Object getData(){
    return this.data;
  }

  public static synchronized Globals getInstance(){
    if(instance==null){
      instance=new Globals();
    }
    return instance;
  }
}
