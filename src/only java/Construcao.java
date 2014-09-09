package com.me.mst;

public class Construcao extends Thread {

	public void run(){
		while (MST.verticenum < Grafo.grafoentrada.vertices.size()){
			System.out.println("O vértice " + MST.verticenum + " se liga a quais vértices? Formato: id_vertice,peso_da_aresta \n *'-1' encerra o processo para o vértice");
		    String entrada = MST.in.nextLine();
		    if (entrada.equals("-1")) MST.verticenum ++;
		    else{
		    	String[] separado = entrada.split(",");
		    	try{
		    		Grafo.grafoentrada.addAI(MST.verticenum, Integer.parseInt(separado[0]), Integer.parseInt(separado[1]));
		    	}catch(Exception e){
		    		System.out.println("Houve um erro no formato: 'vertice,peso'");
		    	}
		    }
		}
		try {
				this.finalize();
			} catch (Throwable e) {}
		System.out.println("Grafo construído, clique na tela gráfica para acompanhar o processo de seleção da árvore geradora mínima.");
	}
	
}
