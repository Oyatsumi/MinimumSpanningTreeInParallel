package com.me.mst;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MST implements ApplicationListener {
	//private OrthographicCamera camera;
	private SpriteBatch batch;
	public static short verticenum = 0;
	protected static Scanner in = new Scanner(System.in);
	private static Construcao ct;
	public ShapeRenderer shapeRenderer;
	private int bfheight;
	private static boolean inicializado = false, primeiraetapa = false;
	
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		bfheight = (int) (h - 2);
		
		//camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		
		Grafo.grafoentrada = new Grafo(0);
				
		System.out.println("Quantos vértices terá o grafo?");
	    String qtd = in.nextLine();
	    
	    short quantidade = (short) Integer.parseInt(qtd);
	    for (int i=1; i<quantidade; i++){
	    	Grafo.grafoentrada.addVI(i);
	    }
	    
	    ct = new Construcao();
	    ct.start();
	    
	    //iniciar as threads
	    Processador.processadores = new ArrayList<Processador>();
	    for (int i=0; i<quantidade; i++){
	    	Processador.processadores.add(new Processador(i));
	    }
	    
	    
	    shapeRenderer = new ShapeRenderer();
	    
	    
	    //Processador de entrada
	    Gdx.input.setInputProcessor(new InputProcessor(){
	    	private int numprocessadores;
	    	
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				
				/*if (Processador.subgrafos == null){
					for (int i=0; i<Processador.processadores.size(); i++){
						Processador.processadores.get(i).start();
					}
				}*/
				
				//desenhar a primeira operação também
				if (!primeiraetapa){
					numprocessadores = Processador.processadores.size();
					primeiraetapa = true;
					return false;
				}
				
				//terminar
				if (Processador.processadores.size() == 1){
					System.out.println("Processamento terminado. Árvore geradora mínima selecionada.");
					return false;
				}
				
				
				//verificar se todos terminaram
				if (Processador.todosTerminaram()){
					boolean achou = true;
					while (achou){ //junta os grafos que contém mesmo vértice em um
						achou = false;
						for (int i=0; i < Processador.processadores.size(); i++){
							for (int j=i+1; j < Processador.processadores.size(); j++){
								if (Processador.processadores.get(i).getGrafo().contemArestaComum(Processador.processadores.get(j).getGrafo())){
									achou = true;
									Processador.processadores.get(i).getGrafo().juntar(Processador.processadores.get(j).getGrafo());
									Processador.processadores.remove(j);
									numprocessadores --;
									System.out.println("O processador "+ numprocessadores + " ficou ocioso.");
								}
							}
						}
					}	
					/*if (Processador.processadores.size() < Processador.subgrafos.size()){
						for (int i=Processador.subgrafos.size(); i < Processador.processadores.size(); i++)
							Processador.processadores.remove(i);
					}*/
					
				
					//inicializando/sincronizando processadores
					for (int i=0; i<Processador.processadores.size(); i++){
						if (!inicializado){
							Processador.processadores.get(i).start();
							inicializado = true;
						}
						else Processador.processadores.get(i).run();
						
					}
				}
				
				System.out.println("Subgrafos:  " + Processador.processadores.size());
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
	    });
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//batch.setProjectionMatrix(camera.combined);
		//shapeRenderer.setProjectionMatrix(camera.combined);
				
		Grafo.grafoentrada.desenhar(shapeRenderer);
		
		if (primeiraetapa)
		for (int i=0; i<Processador.processadores.size(); i++) Processador.processadores.get(i).getGrafo().desenhar(shapeRenderer);
		
		shapeRenderer.end();
		batch.begin();
		
		Grafo.grafoentrada.desenhar(batch);
		if (primeiraetapa) 
			Grafo.bf.draw(batch, Integer.toString(Processador.processadores.size()), 2, bfheight);
			
		/*
		if (inicializado)
		for (int i=0; i<Processador.processadores.size(); i++) Processador.processadores.get(i).getGrafo().desenhar(batch);*/
		
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
