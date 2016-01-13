package com.stelmachk.hibernate.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.stelmachk.hibernate.model.domain.Program;
import com.stelmachk.hibernate.model.domain.Stacja;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = { "classpath:/beans.xml" })
	@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
	@Transactional
public class ManagerTest {

	@Autowired
	Manager m; //Wywołanie interfejsu manager

	private final String stacja1 = "stacja1";
	private final String opis1 = "opis1";
	private final String stacja2 = "stacja2";
	private final String opis2 = "opis2";
	private final String program1 = "program1";
	private final String rodzaj1 = "rodzaj1";
	private final String program2 = "program2";
	private final String rodzaj2 = "rodzaj2";
	private List<Long> dodaneProgramy = new ArrayList<Long>();
	private List<Long> dodaneStacje = new ArrayList<Long>();

	@Before
	public void sprawdzDodaneElementy() {
		List<Program> programy = m.dajWszystkieProgramy();
		List<Stacja> stacje = m.dajWszystkieStacje();

		for (Program prog : programy)
			dodaneProgramy.add(prog.getId());
		for (Stacja sta : stacje)
			dodaneStacje.add(sta.getId());
	}

	@After
	public void usunTestowaneDane() {
		List<Program> programy = m.dajWszystkieProgramy();
		List<Stacja> stacje = m.dajWszystkieStacje();
		boolean usun;

		for (Program prog : programy) {
			usun = true;
			for (Long prog2 : dodaneProgramy)
				if (prog.getId() == prog2) {
					usun = false;
					break;
				}
			if (usun)
				m.usun(prog);
		}
		for (Stacja sta : stacje) {
			usun = true;
			for (Long sta2 : dodaneStacje)
				if (sta.getId() == sta2) {
					usun = false;
					break;
				}
			if (usun)
				m.usun(sta);
		}
	}

	@Test
	public void sprawdzPobierzPoId() {
		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);
		
		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);

		Long sId = m.dodaj(s);
		Long pId = m.dodaj(p);

		Program ps = m.pobierzProgramPoId(pId);
		Stacja ss = m.pobierzStacjePoId(sId);

		assertEquals(sId, ss.getId());
		assertEquals(stacja1, ss.getStacja());
		assertEquals(opis1, ss.getOpis());
		assertEquals(pId, ps.getId());
		assertEquals(s.getStacja(), ps.getStacja().getStacja());
		assertEquals(s.getOpis(), ps.getStacja().getOpis());
		assertEquals(program1, ps.getNazwa());
		assertEquals(rodzaj1, ps.getRodzaj());

	}

	@Test
	public void sprawdzDodaj() {
		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);

		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);

		Long sId = m.dodaj(s);
		Long pId = m.dodaj(p);

		Program ps = m.pobierzProgramPoId(pId);
		Stacja ss = m.pobierzStacjePoId(sId);

		assertEquals(stacja1, ss.getStacja());
		assertEquals(opis1, ss.getOpis());

		assertEquals(stacja1, ps.getStacja().getStacja());
		assertEquals(opis1, ps.getStacja().getOpis());
		assertEquals(program1, ps.getNazwa());
		assertEquals(rodzaj1, ps.getRodzaj());

	}

	@Test
	public void sprawdzEdytuj() {

		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);

		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);

		Long sId = m.dodaj(s);
		Long pId = m.dodaj(p);

		List<Program> programy = m.dajWszystkieProgramy();
		List<Stacja> stacje = m.dajWszystkieStacje();

		m.edytuj(s, stacja2, opis2);
		m.edytuj(p, s, program2, rodzaj2);

		int i = 0;
		int j = 0;

		List<Program> programy2 = m.dajWszystkieProgramy();
		List<Stacja> stacje2 = m.dajWszystkieStacje();

		for (Program prog : programy) {
			for (Program prog2 : programy2) {
				if (prog.getId() == prog2.getId()) {
					if (prog.getId() != pId) {
						assertEquals(prog2.getStacja().getStacja(), prog.getStacja().getStacja());
						assertEquals(prog2.getStacja().getOpis(), prog.getStacja().getOpis());
						assertEquals(prog2.getNazwa(), prog.getNazwa());
						assertEquals(prog2.getRodzaj(), prog.getRodzaj());
						i++;
					} else if (prog.getId() == pId) {
						assertEquals(stacja2, prog.getStacja().getStacja());
						assertEquals(opis2, prog.getStacja().getOpis());
						assertEquals(program2, prog.getNazwa());
						assertEquals(rodzaj2, prog.getRodzaj());
						j++;
					}
				}
			}
		}
		assertEquals(j, 1);
		assertEquals(i + j, programy2.size());
		assertEquals(programy.size(), programy2.size());

		i = 0;
		j = 0;

		for (Stacja sta : stacje) {
			for (Stacja sta2 : stacje2) {
				if (sta.getId() == sta2.getId()) {
					if (sta.getId() != sId) {
						assertEquals(stacje2.get(i).getStacja(), sta.getStacja());
						assertEquals(stacje2.get(i).getOpis(), sta.getOpis());
						i++;
					} else if (sta.getId() == sId) {
						assertEquals(stacja2, sta.getStacja());
						assertEquals(opis2, sta.getOpis());
						j++;
					}
				}
			}
		}
		assertEquals(j, 1);
		assertEquals(i + j, stacje2.size());
		assertEquals(stacje.size(), stacje2.size());
	}

	@Test
	public void sprawdzUsun() {
		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);

		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);
		
		Long sId = m.dodaj(s);
		Long pId = m.dodaj(p);

		List<Program> programy = m.dajWszystkieProgramy();
		List<Stacja> stacje = m.dajWszystkieStacje();
		m.usun(p);
		m.usun(s);

		int ileP = programy.size();
		int ileS = stacje.size();

		Program ps = m.pobierzProgramPoId(pId);
		Stacja ss = m.pobierzStacjePoId(sId);
		assertEquals(ps, null);
		assertEquals(ss, null);

		List<Program> programy2 = m.dajWszystkieProgramy();
		List<Stacja> stacje2 = m.dajWszystkieStacje();
		assertEquals(programy2.size(), ileP - 1);
		assertEquals(stacje2.size(), ileS - 1);

		int i = 0;

		for (Program prog : programy) {
			for (Program prog2 : programy2)
				if (prog.getId() == prog2.getId()) {
					assertEquals(prog2.getStacja().getStacja(), prog.getStacja().getStacja());
					assertEquals(prog2.getStacja().getOpis(), prog.getStacja().getOpis());
					assertEquals(prog2.getNazwa(), prog.getNazwa());
					assertEquals(prog2.getRodzaj(), prog.getRodzaj());
					i++;
				}
		}

		assertEquals(programy2.size(), i);

		i = 0;

		for (Stacja sta : stacje) {
			for (Stacja sta2 : stacje2) {
				if (sta.getId() == sta2.getId()) {
					assertEquals(sta2.getStacja(), sta.getStacja());
					assertEquals(sta2.getOpis(), sta.getOpis());
					i++;
				}
			}
		}
		assertEquals(stacje2.size(), i);
	}

	@Test
	public void sprawdzDajWszystkie() {

		List<Program> programy = m.dajWszystkieProgramy();
		List<Stacja> stacje = m.dajWszystkieStacje();
		int ileP = programy.size();
		int ileS = stacje.size();

		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);

		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);
		m.dodaj(s);
		m.dodaj(p);

		programy = m.dajWszystkieProgramy();
		stacje = m.dajWszystkieStacje();
		assertEquals(ileP + 1, programy.size());
		assertEquals(ileS + 1, stacje.size());

		for (Program prog : programy) {
			p = m.pobierzProgramPoId(prog.getId());
			assertNotNull(p);
		}
		for (Stacja sta : stacje) {
			s = m.pobierzStacjePoId(sta.getId());
			assertNotNull(s);
		}
	}

	@Test
	public void sprawdzWyszukajProgramyWgWzorcaStacjii() {

		Stacja s = new Stacja();
		s.setStacja(stacja1);
		s.setOpis(opis1);

		Program p = new Program();
		p.setStacja(s);
		p.setNazwa(program1);
		p.setRodzaj(rodzaj1);
		m.dodaj(s);
		m.dodaj(p);

		String wzor = stacja1;
		int ile = 0;

		for (Long l : dodaneProgramy) {
			if (Pattern.compile(".*" + wzor + ".*").matcher(m.pobierzProgramPoId(l).getStacja().getStacja()).matches())
				ile++;
		}
		List<Program> lp = m.wyszukajProgramyWgWzorcaStacjii(wzor); 
		assertEquals(lp.size(), ile + 1);
	}

	@Test
	public void sprawdzWyszukajProgramy() {

		Stacja s1 = new Stacja();
		Stacja s2 = new Stacja();
		s1.setStacja(stacja1);
		s1.setOpis(opis1);
		s2.setStacja(stacja2);
		s2.setOpis(opis2);

		m.dodaj(s1);
		m.dodaj(s2);

		Program p1 = new Program();
		Program p2 = new Program();

		p1.setStacja(s1);
		p1.setNazwa(program1);
		p1.setRodzaj(rodzaj1);
		p2.setStacja(s1);
		p2.setNazwa(program2);
		p2.setRodzaj(rodzaj2);
		m.dodaj(p1);
		m.dodaj(p2);

		assertEquals(m.wyszukajProgramy(s1).size(), 2);
		for (Program p : m.wyszukajProgramy(s1)) {
			assertEquals(s1.getId(), p.getStacja().getId());
			assertEquals(s1.getStacja(), p.getStacja().getStacja());
			assertEquals(s1.getOpis(), p.getStacja().getOpis());
		}
		assertEquals(m.wyszukajProgramy(s2).size(), 0);

	}

	@Test
	public void sprawdzUsunZaleznosci() {

		Stacja s1 = new Stacja();

		s1.setStacja(stacja1);
		s1.setOpis(opis1);
		m.dodaj(s1);
		Program p1 = new Program();
		Program p2 = new Program();
		p1.setStacja(s1);
		p1.setNazwa(program1);
		p1.setRodzaj(rodzaj1);
		p2.setStacja(s1);
		p2.setNazwa(program2);
		p2.setRodzaj(rodzaj2);

		Long idP1 = m.dodaj(p1);
		Long idP2 = m.dodaj(p2);

		List<Program> programy = m.dajWszystkieProgramy();
		m.usunZaleznosci(s1);
		
		Program ps1 = m.pobierzProgramPoId(idP1);
		Program ps2 = m.pobierzProgramPoId(idP2);
		assertEquals(ps1, null);
		assertEquals(ps2, null);
		
		List<Program> programy2 = m.dajWszystkieProgramy();
		assertEquals(programy2.size(), programy.size() - 2);
		
		int i = 0;
		for (Program prog : programy) {
			for (Program prog2 : programy2)
				if (prog.getId() == prog2.getId()) {
					assertEquals(prog2.getStacja().getStacja(), prog.getStacja().getStacja());
					assertEquals(prog2.getStacja().getOpis(), prog.getStacja().getOpis());
					assertEquals(prog2.getNazwa(), prog.getNazwa());
					assertEquals(prog2.getRodzaj(), prog.getRodzaj());
					i++;
				}
		}
		assertEquals(programy2.size(), i);
	}

	@Test
	public void sprawdzUsuwanieKaskadowe() {
		Stacja s1 = new Stacja();
		s1.setStacja(stacja1);
		s1.setOpis(opis1);

		m.dodaj(s1);

		Program p1 = new Program();
		Program p2 = new Program();
		p1.setStacja(s1);
		p1.setNazwa(program1);
		p1.setRodzaj(rodzaj1);
		p2.setStacja(s1);
		p2.setNazwa(program2);
		p2.setRodzaj(rodzaj2);

		Long idP1 = m.dodaj(p1);
		Long idP2 = m.dodaj(p2);

		List<Program> programy = m.dajWszystkieProgramy();

		m.usun(s1);
		Program ps1 = m.pobierzProgramPoId(idP1);
		Program ps2 = m.pobierzProgramPoId(idP2);

		assertEquals(ps1, null);
		assertEquals(ps2, null);

		List<Program> programy2 = m.dajWszystkieProgramy();

		assertEquals(programy2.size(), programy.size() - 2);

		int i = 0;
		for (Program prog : programy) {
			for (Program prog2 : programy2)
				if (prog.getId() == prog2.getId()) {
					assertEquals(prog2.getStacja().getStacja(), prog.getStacja().getStacja());
					assertEquals(prog2.getStacja().getOpis(), prog.getStacja().getOpis());
					assertEquals(prog2.getNazwa(), prog.getNazwa());
					assertEquals(prog2.getRodzaj(), prog.getRodzaj());
					i++;
				}
		}
		assertEquals(programy2.size(), i);
	}
}
