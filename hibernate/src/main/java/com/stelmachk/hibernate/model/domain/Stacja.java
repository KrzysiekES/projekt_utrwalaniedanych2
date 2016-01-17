package com.stelmachk.hibernate.model.domain;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

	@Entity
	@NamedQueries({
	        @NamedQuery(name = "stacja.wszystkie", query = "SELECT s FROM Stacja s"),
	})

public class Stacja {
    private Long id;
    private String stacja;
    private String opis;
    private List<Program> programy = new ArrayList<Program>();
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "stacja", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Program> getProgramy() {
        return programy;
    }
  
    public void setProgramy(List<Program> programy) {
        this.programy = programy;
    }

    @Column(nullable = false)
    public String getStacja() {
        return stacja;
    }
  
    public void setStacja(String stacja) {
        this.stacja = stacja;
    }

    public String getOpis() {
        return opis;
    }
    public void setOpis(String opis) {
        this.opis = opis;
    }
}

