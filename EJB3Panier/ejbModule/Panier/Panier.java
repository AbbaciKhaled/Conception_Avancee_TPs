package Panier;

import java.util.Vector;

import javax.ejb.Remote;

@Remote
public interface Panier {
	public void ajouterArticle(int idArticle);
	public void supprimerArticle(int idArticle);
	public Vector listArticles();
	public void setNom(String nomClient);
	public String getNom();
	public void remove();
}
