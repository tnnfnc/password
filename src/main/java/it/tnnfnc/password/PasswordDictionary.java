/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.password;

import it.tnnfnc.apps.application.ui.Utility;
import it.tnnfnc.password.domain.Access;
import it.tnnfnc.password.domain.AccessFactory;

public class PasswordDictionary {

	public static class Rule {
		String s_name;
		String[] s_rule;

		public String getName() {
			return s_name;
		}

		public String toString() {
			return s_name;
		}

		public String[] getRule() {
			return s_rule;
		}

	}

	private static Rule[] rules = new Rule[] { new Empty(), new Numbers(), new Letters(), new Symbols(),
			new Tokens(), };

	public static String[] EnglishSyllables = {};

	/**
	 * Fill an array with the symbols contained in the ranges.
	 * 
	 * @param range
	 *            the array of symbol ranges
	 * 
	 * @return the array
	 */
	public static String[] getDictionary(Access access) {

		access.getFactory();
		String[] password_rules = (access.getValue(AccessFactory.PASSWORD_TYPE) + "").split("\\.");

		Object[] codes = new Object[0];

		// NEW CODE
		for (int i = 1; i < password_rules.length; i++) {
			codes = Utility.addArray(codes, getDictionary(password_rules[i]).getRule());
		}

		// encode table
		String[] array = new String[codes.length];

		// fill the table of consecutive numbers
		int i = 0;
		for (Object o : codes) {
			array[i++] = o + "";
		}
		return array;
	}

	/**
	 * @param string
	 * @return
	 */
	public static Rule getDictionary(String string) {
		for (Rule r : rules) {
			if (r.getName().equalsIgnoreCase(string)) {
				return r;
			}
		}
		return new Empty();

	}

	public static String[] getDictionaries() {
		String[] s = new String[rules.length];
		for (int i = 0; i < rules.length; i++) {
			s[i] = rules[i].getName();
		}
		return s;
	}

	public static class Empty extends Rule {
		public static String name = " - - - ";
		public static String[] dictionary = new String[0];

		public Empty() {
			s_name = name;
			s_rule = dictionary;
		}

	}

	public static class Numbers extends Rule {
		public static String name = "Numbers";
		public static String[] dictionary = { //
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", //
		};

		public Numbers() {
			s_name = name;
			s_rule = dictionary;
		}

	}

	public static class Letters extends Rule {
		public static String name = "Letters";

		public static String[] dictionary = { //
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
				"v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", //
		};

		public Letters() {
			s_name = name;
			s_rule = dictionary;
		}

	}

	public static class Symbols extends Rule {
		public static String name = "Symbols";
		public static String[] dictionary = { //
				"!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">",
				"?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~",
				//
		};

		public Symbols() {
			s_name = name;
			s_rule = dictionary;
		}

	}

	public static class Tokens extends Rule {
		public static String name = "Tokens";
		public static String[] dictionary = { "a", "ab", "ac", "achie", "ad", "af", "ag", "ai", "al", "ale", "ali",
				"am", "ami", "an", "ana", "ap", "ar", "as", "astro", "at", "au", "av", "avo", "b", "ba", "bar", "bat",
				"be", "bel", "ben", "bi", "bia", "bib", "biet", "bil", "bio", "ble", "bli", "blio", "bloc", "bnor",
				"bo", "boc", "bor", "bra", "brac", "bri", "bro", "bru", "bu", "bui", "c", "ca", "cac", "cam", "can",
				"car", "cas", "ce", "cei", "cel", "cen", "cer", "ces", "cet", "cfr", "ch", "che", "chi", "chia", "chie",
				"chiu", "ci", "cia", "cie", "cin", "cio", "cir", "cit", "clas", "cle", "clo", "clu", "co", "col", "com",
				"con", "conv", "cor", "cra", "cre", "cri", "cro", "cu", "cui", "cul", "cun", "cuo", "d", "da", "dal",
				"dan", "dat", "de", "deb", "dei", "del", "den", "der", "des", "det", "di", "dia", "die", "dien", "dif",
				"dio", "do", "dos", "dot", "dran", "dre", "dri", "dro", "du", "dul", "dur", "dut", "dyaus", "e", "eb",
				"ebi", "ec", "ed", "edo", "ef", "ei", "el", "en", "eri", "es", "espres", "espri", "estre", "estro",
				"eu", "extra", "extrau", "f", "fa", "fal", "fan", "fat", "fe", "fec", "fel", "fer", "fet", "fi", "fia",
				"fiac", "fie", "fin", "fio", "fle", "fles", "flit", "fnir", "fo", "fon", "for", "fra", "fran", "fre",
				"frir", "fro", "fron", "frui", "fu", "fui", "ful", "fun", "g", "ga", "gam", "gan", "gar", "ge", "gen",
				"ger", "get", "gfri", "gi", "gia", "giar", "gie", "gio", "git", "giu", "giun", "gli", "glia", "glian",
				"glie", "glier", "glio", "gliuol", "gna", "gnan", "gni", "gnir", "go", "gos", "gra", "gran", "gre",
				"grei", "gri", "gu", "gua", "guag", "guar", "gue", "guen", "gui", "guo", "ha", "han", "hoc", "hrun",
				"i", "ia", "iet", "ii", "il", "im", "in", "io", "iu", "iup", "iv", "l", "la", "lac", "lal", "lam",
				"lan", "lap", "lar", "las", "lat", "lau", "le", "led", "leg", "len", "ler", "let", "li", "lia", "lie",
				"lim", "lin", "lio", "lis", "liz", "lo", "loc", "lol", "lom", "lon", "lor", "lot", "lu", "lui", "lun",
				"luo", "lup", "m", "ma", "mal", "man", "mar", "mas", "me", "men", "mes", "met", "mez", "mi", "mia",
				"mil", "min", "mis", "miz", "ml", "mo", "mol", "mon", "mor", "mos", "mu", "mun", "n", "na", "nag",
				"nal", "nan", "nar", "nau", "ne", "nei", "nel", "nen", "ner", "net", "ni", "nia", "nio", "nis", "no",
				"non", "not", "nu", "nua", "nun", "nuo", "o", "ob", "obe", "oblog", "oc", "of", "ogo", "ol", "ole",
				"olo", "om", "ome", "on", "op", "oper", "or", "os", "ot", "ovo", "p", "pa", "pal", "pan", "par", "pas",
				"pdf", "pe", "pec", "peg", "pel", "pen", "per", "pi", "pia", "pie", "pien", "pin", "pio", "piog", "pit",
				"piu", "pla", "ple", "ples", "pli", "plo", "plu", "po", "poi", "pol", "por", "pos", "pra", "prat",
				"prav", "pre", "prei", "pren", "pres", "pri", "prin", "prio", "pro", "proc", "pros", "pu", "pub", "pun",
				"pur", "puz", "qu", "qua", "qual", "quan", "que", "quel", "quen", "qui", "quin", "quo", "r", "ra",
				"rac", "raf", "rag", "ral", "ram", "ran", "rap", "rat", "re", "reb", "reg", "ren", "rer", "ret", "ri",
				"ria", "ric", "rio", "ris", "riz", "ro", "roc", "roi", "ros", "roz", "ru", "s", "sa", "sag", "sal",
				"sam", "san", "sat", "sau", "sbi", "sca", "sce", "scel", "scen", "sche", "schiet", "sci", "scia",
				"scien", "scio", "sco", "scol", "scon", "scor", "scri", "scrit", "scuo", "scus", "se", "sec", "sel",
				"sem", "sen", "sep", "ser", "sfa", "sfe", "sfor", "sgre", "si", "sia", "sid", "sie", "sim", "sin",
				"sio", "smar", "smi", "smo", "so", "sod", "sol", "som", "son", "soq", "sor", "sot", "spa", "spe",
				"spec", "spes", "spet", "spi", "spie", "spin", "spo", "spon", "sta", "stan", "star", "stau", "ste",
				"stel", "sten", "ster", "stes", "stespres", "sti", "stia", "stin", "sto", "stra", "strel", "stret",
				"stri", "stro", "stru", "struosi", "stu", "su", "sud", "sug", "sui", "sul", "suo", "suoi", "sur", "svi",
				"t", "ta", "tal", "tan", "tar", "te", "tec", "tel", "tem", "ten", "ter", "tes", "tez", "thor", "ti",
				"tia", "tie", "tin", "tio", "tiz", "tna", "to", "tom", "tor", "tr", "tra", "trat", "tre", "tren", "tri",
				"tro", "trop", "tru", "tu", "tui", "tur", "tut", "tylor", "u", "uc", "ul", "un", "una", "unal", "unan",
				"unat", "unu", "uo", "v", "va", "val", "van", "ve", "vec", "vel", "ven", "ver", "ves", "vi", "via",
				"viag", "vii", "vil", "vin", "vio", "vir", "vis", "vo", "vol", "vreb", "vu", "vul", "web", "xi", "z",
				"za", "ze", "zeus", "zez", "zi", "zia", "zial", "zie", "zio", "zo", "zop", "zuc" };

		public Tokens() {
			getDictionary();
			s_name = name;
			s_rule = dictionary;
		}

		private static void getDictionary() {
			String string = new String(); // dictionary
			for (int i = 0; i < dictionary.length; i++) {
				string = dictionary[i];
				if (string.length() > 2)
					string = new String(string.substring(0, 1).toUpperCase() + string.substring(1));
				if (string.length() == 3)
					string = new String(string + (i + "").substring(0, 1));
				dictionary[i] = string;
			}
		}

	}

}
