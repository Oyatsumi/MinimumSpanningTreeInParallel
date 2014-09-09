package com.me.mst;

public class Construcao extends Thread {

	public void run(){
		while (MST.verticenum < Grafo.grafoentrada.vertices.size()){
			System.out.println("O v�rtice " + MST.verticenum + " se liga a quais v�rtices? Formato: id_vertice,peso_da_aresta \n *'-1' encerra o processo para o v�rtice");
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
		System.out.println("Grafo constru�do, clique na tela gr�fica para acompanhar o processo de sele��o da �rvore geradora m�nima.");
	}
	
}
