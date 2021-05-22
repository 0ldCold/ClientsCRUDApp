package model.entity;

public class AccessLvl {
    private Long id;
    private String title;

    public AccessLvl(){}

    public AccessLvl(Long id, String title){
        this.id = id;
        this.title = title;
    }



    public Long getId(){
        return this.id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public String toString(){
        return this.title;
    }

    public Long toLong(){
        return this.id;
    }
}
