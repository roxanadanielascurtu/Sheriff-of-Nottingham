package com.tema1.main;

import com.tema1.goods.LegalGoods;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.Goods;
import com.tema1.common.Constants;
import java.util.*;
import java.lang.Math;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic
        List<String> players = gameInput.getPlayerNames();
        List<Integer> buget = new ArrayList<>(players.size());
        Map<Integer, Integer> kingbonus = new HashMap<>(players.size());
        Map<Integer, Integer> queenbonus = new HashMap<>(players.size());
        List<Map<Integer, Integer>> bonus = new ArrayList<Map<Integer, Integer>>();
        Map<Integer, Integer> bonusi = new HashMap<>(players.size());	
			
        for (int i = 0; i < players.size(); i++) {
            bonusi.put(i, 0);
				}
        for (int i = 0; i < Constants.nlgoods; i++) {
            Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();
            for (int j = 0; j < players.size(); j++) {
                hashmap.put(j, 0);
						}
            bonus.add(hashmap);
        }
        for (int i = 0; i < players.size(); i++) {
            kingbonus.put(i, 0);
				}
        for (int i = 0; i < players.size(); i++) {
            queenbonus.put(i, 0);
				}
        GoodsFactory goodsfactory = GoodsFactory.getInstance();
        int fromIndex = 0;
        int toIndex = Constants.nlgoods;
        for (int i = 0; i < players.size(); i++) {
            buget.add(Constants.BUGETINITIAL);
        }
        int serif = 0;
        for (int i = 1; i <= gameInput.getRounds(); i++) {
            for (int j = 0; j < players.size(); j++) {
                serif = j;
                for (int k = 0; k < players.size(); k++) {
                    /////comerciant
                    if (serif != k) {
                        int mita = 0;
                        List<Integer> comm = gameInput.getAssetIds().subList(fromIndex, toIndex);
                        Map<Integer, Integer> sac = new HashMap<>();
                        Map<Integer, Integer> lgoods = new HashMap<>();
                        Map<Integer, Integer> igoods = new HashMap<>();
                        Map<Integer, List<Integer>> hash = new TreeMap<Integer,
                                List<Integer>>(Collections.reverseOrder());
                        fromIndex += Constants.nlgoods;
                        toIndex += Constants.nlgoods;
                        int declare = -1;
                        for (int m = 0; m < comm.size(); m++) {
                            if (comm.get(m) < Constants.nlgoods) {
                                int l = comm.get(m);
                                if (lgoods.containsKey(l)) {
                                    int nr = lgoods.get(l);
                                    lgoods.put(l, ++nr);
                                } else
                                    lgoods.put(l, 1);
                            } else {
                                int il = comm.get(m);
                                if (igoods.containsKey(il)) {
                                    int nr = igoods.get(il);
                                    igoods.put(il, ++nr);
                                } else
                                    igoods.put(il, 1);
                            }
                        }

                        /////LegalGoods
                        if (lgoods.size() != 0) {
                            if (!(players.get(k).equals("bribed") && igoods.size() > 0)) {
                                int maxapp = 0;
                                for (Integer value : lgoods.values()) {
                                    if (value >= maxapp)
                                        maxapp = value;
                                }
                                for (Integer key : lgoods.keySet()) {
                                    if (lgoods.get(key) == maxapp) {
                                        if (declare == -1)
                                            declare = key;
                                        else {
                                            declare = -1;
                                            break;
                                        }
                                    }
                                }
                                if (declare == -1) {
                                    int maxprofit = 0;
                                    for (Integer key : lgoods.keySet()) {
                                        if (lgoods.get(key) == maxapp && goodsfactory
                                                .getGoodsById(key).getProfit() > maxprofit)
                                            maxprofit = goodsfactory.getGoodsById(key).getProfit();
                                    }
                                    for (Integer key : lgoods.keySet()) {
                                        if (lgoods.get(key) == maxapp && goodsfactory
                                                .getGoodsById(key).getProfit() == maxprofit) {
                                            if (declare == -1)
                                                declare = key;
                                            else {
                                                declare = -1;
                                                break;
                                            }
                                        }
                                    }
                                    if (declare == -1) {
                                        int maxid = -1;
                                        for (Integer key : lgoods.keySet()) {
                                            if (lgoods.get(key) == maxapp && goodsfactory
                                                    .getGoodsById(key).getProfit() == maxprofit
                                                    && goodsfactory.getGoodsById(key)
                                                    .getId() > maxid) {
                                                maxid = goodsfactory.getGoodsById(key).getId();
                                                declare = key;
                                            }
                                        }
                                    }
                                }
                                sac.put(declare, Math.min(Constants.MINDEC, lgoods.get(declare)));
                            }
                        }
                        /////////Illegalgoods
                        int penalty = 0;
                        if (igoods.size() != 0) {
                            int maxprofit = 0;
                            int ilgood = -1;
														
                            if (players.get(k).equals("basic") || players.get(k).equals("greedy")
                                    || (players.get(k).equals("bribed") && buget.get(k) < Constants.MINIMI)) {
                                for (Integer key : igoods.keySet()) {
                                    if (goodsfactory.getGoodsById(key).getProfit() > maxprofit) {
                                        maxprofit = goodsfactory.getGoodsById(key).getProfit();
                                        ilgood = key;
                                    }
                                }
                                int nr = 0;
                                if (ilgood != -1) {
                                    penalty += goodsfactory.getGoodsById(ilgood).getPenalty();
                                    if (penalty < buget.get(k)) {
                                        if (sac.containsKey(ilgood)) {
                                            nr = sac.get(ilgood);
                                            nr++;
                                            sac.put(ilgood, nr);
                                        } else
                                            sac.put(ilgood, 1);
                                    }
                                }
                                declare = 0;
                            } else {
                                int nri = 0;
                                int total = 0;
                                if (buget.get(k) >= Constants.MINIMI && buget.get(k) < Constants.MINIMII) {
                                    mita = 5;
                                    nri = 1;
                                } else if (buget.get(k) >= Constants.MINIMII && buget.get(k) <= Constants.MINIM3) {
                                    mita = 5;
                                    nri = 2;
                                } else {
                                    mita = 10;
                                    nri = Math.min(Constants.MINDEC, (buget.get(k) - mita) / 2);
                                }

                                for (Integer key : igoods.keySet()) {
                                    List<Integer> list = new ArrayList<Integer>();
                                    list.add(key);
                                    list.add(igoods.get(key));
                                    hash.put(goodsfactory.getGoodsById(key).getProfit(), list);
                                }
                                int maximum = 0;
                                for (Integer key : hash.keySet()) {
                                    int a = hash.get(key).get(1);
                                    if (total + a < nri) {
                                        maximum += hash.get(key).get(0) * a;
                                        total += a;
                                        sac.put(hash.get(key).get(0), a);
                                    } else {

                                        maximum += hash.get(key).get(0) * (nri - total);
                                        sac.put(hash.get(key).get(0), nri - total);
                                        break;
                                    }
                                }
                                for (Integer key : lgoods.keySet()) {
                                    ArrayList<Integer> list1 = new ArrayList<Integer>();
                                    ArrayList<Integer> list2 = new ArrayList<Integer>();
                                    list1.add(key);
                                    list1.add(igoods.get(key));
                                    list2.add(goodsfactory.getGoodsById(key).getProfit());
                                    list2.add(goodsfactory.getGoodsById(key).getId());

                                }


                            }
                        }
                        if (i % 2 == 0 && players.get(k).equals("greedy")) {
                            int number = 0;
                            int maxprofit = 0;
                            int ilgood = -1;
                            for (Integer value : sac.values()) {
                                number += value;
                            }
                            if (number < Constants.MINDEC) {
                                for (Integer key : igoods.keySet()) {
                                    if (goodsfactory.getGoodsById(key).getProfit() > maxprofit) {
                                        maxprofit = goodsfactory.getGoodsById(key
                                        ).getProfit();
                                        ilgood = key;
                                    }
                                }
                                int nr = 0;
                                if (ilgood != -1) {
                                    penalty += goodsfactory.getGoodsById(ilgood).getPenalty();
                                    if (penalty < buget.get(k)) {
                                        if (sac.containsKey(ilgood)) {
                                            nr = sac.get(ilgood);
                                            nr++;
                                            sac.put(ilgood, nr);
                                        } else
                                            sac.put(ilgood, 1);
                                    }
                                }
                            }
                        }
                        ///////Basic and Greedy Strategy

                        ///////Bribed Strategy


                        ///////////////////////////// Inspectia serifului
                        int stanga = 0;
                        int dreapta = 0;
                        if (serif == 0) {
                            stanga = players.size() - 1;
                            dreapta = 1;
                        } else if (serif == players.size() - 1) {
                            stanga = players.size() - 2;
                            dreapta = 0;
                        } else {
                            stanga = serif - 1;
                            dreapta = serif + 1;
                        }
                        if (mita != 0) {

                            if (players.get(serif).equals("greedy") || (players.get(serif)
                                    .equals("bribed") && (k != dreapta || k != stanga))) {
                                buget.set(k, buget.get(k) - mita);
                                buget.set(serif, buget.get(serif) + mita);
                            }
                        }

                        for (Integer key : sac.keySet()) {
                            String tip = players.get(serif);

                            if (goodsfactory.getGoodsById(key).getId() < Constants.nlgoods) {
                                int nr1 = goodsfactory.getGoodsById(key).getPenalty() * sac.get(key);
                                int nr2 = goodsfactory.getGoodsById(key).getProfit() * sac.get(key);
                                if (buget.get(serif) >= Constants.BUGETMINIM && (tip.equals("basic")
                                        || (tip.equals("greedy") && mita == 0)
                                        || (tip.equals("bribed") && ((k == dreapta || k == stanga)
                                        || mita == 0)))) {
                                    if (key == declare) {
                                        buget.set(k, buget.get(k) + nr1);
                                        buget.set(serif, buget.get(serif) - nr1);
                                        buget.set(k, buget.get(k) + nr2);
                                    } else {
                                        buget.set(k, buget.get(k) - nr1);
                                        buget.set(serif, buget.get(serif) + nr1);
                                    }
                                } else {
                                    buget.set(k, buget.get(k) + nr2);
                                }
                                int nr = bonus.get(key).get(k) + sac.get(key);
                                bonus.get(key).put(k, nr);
                            } else {
                                IllegalGoods l = (IllegalGoods) (goodsfactory.getGoodsById(key));

                                if (buget.get(serif) >= Constants.BUGETMINIM && (tip.equals("basic")
                                        || (tip.equals("bribed") && ((k == dreapta || k == stanga)
                                        || mita == 0) || (tip.equals("greedy") && mita == 0)))) {
                                    int nr1 = l.getPenalty() * sac.get(key);

                                    buget.set(k, buget.get(k) - nr1);
                                    buget.set(serif, buget.get(serif) + nr1);

                                } else {
                                    int nr1 = l.getProfit();
                                    Map<Goods, Integer> bonuses = l.getIllegalBonus();
                                    buget.set(k, buget.get(k) + l.getProfit() * sac.get(key));
                                    int bon = 0;
                                    for (Goods b : bonuses.keySet()) {
                                        bon += b.getProfit() * bonuses.get(b);
                                        int nr = bonus.get(b.getId()).get(k) + sac.get(key);
                                        bonus.get(b.getId()).put(k, nr);
                                    }
                                    bonusi.put(k, bonusi.get(k) + bon * sac.get(key));

                                }
                            }
                        }

                        ////////////comerciant
                    } else
                        continue;


                }
            }
        }

        for (int i = 0; i < bonus.size(); i++) {
            Map<Integer, Integer> aux = bonus.get(i);
            int maxim1 = 0;
            int maxim2 = 0;
            int index1 = -1;
            int index2 = -1;

            for (int j = 0; j < aux.size(); j++) {

                if (maxim1 < aux.get(j)) {
                    maxim1 = aux.get(j);
                    index1 = j;
                }

            }
            for (int j = 0; j < aux.size(); j++) {
                if (aux.get(j) > maxim2 && index1 != j && aux.get(j) != 0) {
                    maxim2 = aux.get(j);
                    index2 = j;
                }
            }
            LegalGoods legalgood = (LegalGoods) goodsfactory.getGoodsById(i);
            if (index1 != -1) {
                int nr = buget.get(index1);
                buget.set(index1, nr + legalgood.getKingBonus());
            }
            if (index2 != -1) {
                int nr = buget.get(index2);
                buget.set(index2, nr + legalgood.getQueenBonus());
            }
        }

        for (int j = 0; j < bonusi.size(); j++) {
            buget.set(j, buget.get(j) + bonusi.get(j));
        }


        Map<Integer, List<Integer>> clasament = new TreeMap<Integer, List<Integer>>(Collections
                .reverseOrder());
        for (int i = 0; i < buget.size(); i++) {
            if (clasament.get(buget.get(i)) == null) {
                List<Integer> l = new ArrayList<Integer>();
                l.add(i);
                clasament.put(buget.get(i), l);
            } else {
                List<Integer> l = clasament.get(buget.get(i));
                l.add(i);
                clasament.put(buget.get(i), l);
            }
        }

        for (Integer key : clasament.keySet()) {
            List<Integer> l = clasament.get(key);
            for (int i = 0; i < l.size(); i++) {
                System.out.println(clasament.get(key).get(i) + " "
                        + players.get(clasament.get(key).get(i)).toUpperCase() + " " + key);
            }
        }

    }
}
