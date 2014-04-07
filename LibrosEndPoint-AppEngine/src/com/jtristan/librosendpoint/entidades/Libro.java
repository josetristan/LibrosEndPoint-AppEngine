package com.jtristan.librosendpoint.entidades;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.users.User;

/**
 * Clase a persistir
 * @author josemaria.tristan
 *
 */ 
@PersistenceCapable
public class Libro {

	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String titulo;
	@Persistent
	private String autor;
	@Persistent
	private int puntuacion;
	@Persistent
	private String ISBN;
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	@Persistent
	private String valorOculto;
	//Utilizamos el usuario para saber quién graba los libros.
	@ApiResourceProperty(name = "usuario_logeado")
	@Persistent
	private User usuario;	


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public User getUsuario() {
		return usuario;
	}
	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	public String getValorOculto() {
		return valorOculto;
	}
	public void setValorOculto(String valorOculto) {
		this.valorOculto = valorOculto;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}




}
