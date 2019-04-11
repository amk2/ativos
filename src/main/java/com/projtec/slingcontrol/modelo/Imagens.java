package com.projtec.slingcontrol.modelo;



import java.io.InputStream;


import org.apache.commons.lang.builder.ToStringBuilder;

public class Imagens {

		private Integer id;		
		private String nome;		
		private String descricao;
	 
		
		
	    private InputStream imagem ;
		private long tam; 
		private String ext ;
		private String nome_arquivo;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
		
		

		public String getNome_arquivo() {
			return nome_arquivo;
		}

		public void setNome_arquivo(String nomeArquivo) {
			nome_arquivo = nomeArquivo;
		}

		public String getExt() {
			return ext;
		}

		public void setExt(String ext) {
			this.ext = ext;
		}

		public long getTam() {
			return tam;
		}

		public void setTam(long tam) {
			this.tam = tam;
		}

		public InputStream getImagem() {
			return imagem;
		}

		public void setImagem(InputStream imagem) {
			this.imagem = imagem;
		}
		
	

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getId() {
			return id;
		}

		
		
		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
		
		@Override
		public boolean equals(Object obj) {
			
			 Imagens tp = (Imagens) obj;
			
			 return this.id == tp.id;
		}

		public String encodeBuffer(byte[] bytes) {
		
		
			return null;
		}

		public byte[] decodeBuffer(String arquivo) {
		
			return null;
		}
		

	}
