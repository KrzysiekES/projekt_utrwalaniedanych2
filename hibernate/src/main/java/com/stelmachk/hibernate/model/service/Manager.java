package com.stelmachk.hibernate.model.service;
import com.stelmachk.hibernate.model.domain.Program;
import com.stelmachk.hibernate.model.domain.Stacja;

import java.util.List;

public interface Manager {

    Program pobierzProgramPoId(Long id);
    Stacja pobierzStacjePoId(Long id);
    Long dodaj(Program program);
    Long dodaj(Stacja stacja);
    void edytuj(Program p, Stacja stacja, String nazwa, String rodzaj);
    void edytuj(Stacja s, String program, String opis);
    void usun(Program p);
    void usun(Stacja s);
    List<Program> dajWszystkieProgramy();
    List<Stacja> dajWszystkieStacje();
    List<Program> wyszukajProgramyWgWzorcaStacjii(String wzorzec);
    List<Program> wyszukajProgramy(Stacja s);
    void usunZaleznosci(Stacja s);

}
