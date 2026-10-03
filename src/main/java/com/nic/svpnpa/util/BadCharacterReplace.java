/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.svpnpa.util;

/**
 *
 * @author Krishna
 */
public class BadCharacterReplace {

    public String findAllreplacements(String str) {
        String newSTr = "";
        newSTr = str.replaceAll("&", "and");
        newSTr = newSTr.replaceAll("<", " LESSTHAN ");
        newSTr = newSTr.replaceAll(">", " GREATERTHAN ");
        newSTr = newSTr.replaceAll("select", " SEL ");
        newSTr = newSTr.replaceAll("insert", " INSRT ");
        newSTr = newSTr.replaceAll("script", " SCRIPT. ");
        newSTr = newSTr.replaceAll("href", "HRREF");
        newSTr = newSTr.replaceAll("iframe", " I FRAME ");
        newSTr = newSTr.replaceAll("http://", "NICHTTPNIC");
        newSTr = newSTr.replaceAll("https://", "NICHTTPSNIC");
        newSTr = newSTr.replaceAll("html", " HTL ");
        newSTr = newSTr.replaceAll("htm", " HTL. ");
        newSTr = newSTr.replaceAll("hidden", " ");
        newSTr = newSTr.replaceAll("\"", "NICBACKSNIC");
        newSTr = newSTr.replaceAll("\\(", "NICOPENBNIC");
        newSTr = newSTr.replaceAll("\\)", "NICCLOSEBNIC");
        newSTr = newSTr.replaceAll("\\+", "NICPLUSNIC");
        newSTr = newSTr.replaceAll("\\@", "NICATNIC");
        newSTr = newSTr.replaceAll("/", "NSLAN");
        newSTr = newSTr.replaceAll("\\?", "NICQUSTIONNIC");
        newSTr = newSTr.replaceAll("=", "NICEQUALNIC");
        newSTr = newSTr.replaceAll("\\%0D%0A", "NICLINESEPNIC");

        return newSTr;
    }

    public String BackAllreplacements(String str) {
        String newSTr = "";
        newSTr = str.replaceAll("and", "&");
        newSTr = newSTr.replaceAll(" LESSTHAN ", "<");
        newSTr = newSTr.replaceAll(" GREATERTHAN ", ">");
        newSTr = newSTr.replaceAll(" SEL ", "select");
        newSTr = newSTr.replaceAll(" INSRT ", "insert");
        newSTr = newSTr.replaceAll(" SCRIPT. ", "script");
        newSTr = newSTr.replaceAll("HRREF", "href");
        newSTr = newSTr.replaceAll(" I FRAME ", "iframe");
        newSTr = newSTr.replaceAll("NICHTTPNIC", "http://");
        newSTr = newSTr.replaceAll("NICHTTPSNIC", "https://");
        newSTr = newSTr.replaceAll(" HTL ", "html");
        newSTr = newSTr.replaceAll(" HTL. ", "htm");
        newSTr = newSTr.replaceAll("NICBACKSNIC", "\"");
        newSTr = newSTr.replaceAll("NICOPENBNIC", "\\(");
        newSTr = newSTr.replaceAll("NICCLOSEBNIC", "\\)");
        newSTr = newSTr.replaceAll("NICPLUSNIC", "\\+");
        newSTr = newSTr.replaceAll("NICATNIC", "\\@");
        newSTr = newSTr.replaceAll("NSLAN", "/");
        newSTr = newSTr.replaceAll("NICQUSTIONNIC", "?");
        newSTr = newSTr.replaceAll("NICEQUALNIC", "=");
        newSTr = newSTr.replaceAll("NICLINESEPNIC", "\\%0D%0A");

        //newSTr = newSTr.replaceAll("+",  "  " );
        return newSTr;
    }

}
