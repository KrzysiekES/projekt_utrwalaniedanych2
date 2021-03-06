package com.stelmachk.hibernate.model.service;
import com.stelmachk.hibernate.model.domain.Program;
import com.stelmachk.hibernate.model.domain.Stacja;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	@Component
	@Transactional
public class ManagerImpl implements Manager {

	@Autowired
	private SessionFactory sf;
	//Setter i getter
	public SessionFactory getSessionFactory() {
		return sf;
	}
	public void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}
	//Funckje
	@Override //Pobieranie po id dla Programu
	public Program pobierzProgramPoId(Long id) {
		return (Program) sf.getCurrentSession().get(Program.class, id);
	}
	@Override //Pobieranie po id dla Stacji
	public Stacja pobierzStacjePoId(Long id) {
		return (Stacja) sf.getCurrentSession().get(Stacja.class, id);
	}
	@Override // Dodawanie programu
	public Long dodaj(Program program) {
		Long id = (Long) sf.getCurrentSession().save(program);
		program.setId(id);
		Stacja stacja = (Stacja) sf.getCurrentSession().get(Stacja.class, program.getStacja().getId());
		stacja.getProgramy().add(program);
		return id;
	}
	@Override // Dodawanie Stacji
	public Long dodaj(Stacja stacja) {
		Long id = (Long) sf.getCurrentSession().save(stacja);
		stacja.setId(id);
		return id;
	}
	@Override//Edycja Programu
	public void edytuj(Program p, Stacja stacja, String nazwa, String rodzaj) {
		p = (Program) sf.getCurrentSession().get(Program.class, p.getId());
		Stacja s = (Stacja) sf.getCurrentSession().get(Stacja.class, p.getStacja().getId());
		int i = 0;
		for (Program prog : s.getProgramy()) {
			if (prog == p)
				break;
			i++;
		}
		p.setStacja(stacja);
		p.setNazwa(nazwa);
		p.setRodzaj(rodzaj);
		s.getProgramy().set(i, p);
		sf.getCurrentSession().update(p);
	}
	@Override//Edycja Stacji
	public void edytuj(Stacja s, String stacja, String opis) {
		s = (Stacja) sf.getCurrentSession().get(Stacja.class, s.getId());
		s.setStacja(stacja);
		s.setOpis(opis);
		sf.getCurrentSession().update(s);
	}
	@Override //Usuwanie programu
	public void usun(Program p) {
		p = (Program) sf.getCurrentSession().get(Program.class, p.getId());
		Stacja s = (Stacja) sf.getCurrentSession().get(Stacja.class, p.getStacja().getId());
		s.getProgramy().remove(p);
		sf.getCurrentSession().delete(p);
	}
	@Override //Usuwanie Stacji
	public void usun(Stacja s) {
		s = (Stacja) sf.getCurrentSession().get(Stacja.class, s.getId());
		sf.getCurrentSession().delete(s);
	}
	@Override //Wypisywanie wszystkich programow
	@SuppressWarnings("unchecked")
	public List<Program> dajWszystkieProgramy() {
		return sf.getCurrentSession().getNamedQuery("program.wszystkie").list();
	}
	@Override //wypisywanie wszystkich stacji
	@SuppressWarnings("unchecked")
	public List<Stacja> dajWszystkieStacje() {
		return sf.getCurrentSession().getNamedQuery("stacja.wszystkie").list();
	}
	@Override//Wyszukiwanie w stacji danego programu // zmiana na like
	public List<Program> wyszukajProgramyWgWzorcaStacjii(String wzorzec) {
		List<Program> lp = new ArrayList<Program>();
		Pattern pa = Pattern.compile(".*" + wzorzec + ".*");
		Matcher m;
		for (Program p : dajWszystkieProgramy()) {
			m = pa.matcher(p.getStacja().getStacja());
			if (m.matches())
				lp.add(p);
		}
		return lp;
	}
	@Override //Zwykle wyszukiwanie programu
	public List<Program> wyszukajProgramy(Stacja s) {
		List<Program> pw = dajWszystkieProgramy();
		List<Program> p = new ArrayList<Program>();
		for (Program prog : pw)
			if (prog.getStacja().getId() == s.getId())
				p.add(prog);
		return p;
	}

	@Override //Usuwanie zależnosci miedzy tabelami
	public void usunZaleznosci(Stacja s) {
		List<Program> programy = dajWszystkieProgramy();
		for (Program prog : programy) {
			if (prog.getStacja().getId() == s.getId())
				usun(prog);
		}
	}
}
