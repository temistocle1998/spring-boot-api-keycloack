package tech.ec2dlt.backend.entity;

public class User {
	private Long id;
	private String prenom;
	private String nom;
	private String email;
	private String password;

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNom() {
		return nom;
	}
	

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
