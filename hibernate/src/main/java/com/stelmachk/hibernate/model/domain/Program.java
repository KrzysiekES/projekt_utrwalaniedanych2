package com.stelmachk.hibernate.model.domain;

import javax.persistence.*;

  @Entity
  @NamedQueries({
        @NamedQuery(name = "program.wszystkie", query = "SELECT p FROM Program p")
  })


public class Program implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private Stacja stacja;
    private String nazwa;
    private String rodzaj;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_religia", nullable = false)
    public Stacja getStacja() {
        return stacja;
    }
    public void setStacja(Stacja stacja) { this.stacja = stacja; }

    @Column(nullable = false)
    public String getNazwa() {
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getRodzaj() {
        return rodzaj;
    }
    public void setRodzaj(String rodzaj) {
        this.rodzaj = rodzaj;
    }
}
