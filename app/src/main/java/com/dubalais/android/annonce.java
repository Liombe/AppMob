package com.dubalais.android;

/**
 * Created by nikola on 01/03/18.
 */

public class annonce {
    private String description;
    //private String prix;
    private String titre;

    public annonce(String titre,String desc/*,String prix*/){
        this.titre=titre;
        this.description=desc;
        //this.prix=prix;
    }
    public annonce(){

    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   /* public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {

        this.prix = prix;
    }
    */
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
