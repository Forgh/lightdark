����   2 R  #com/me/lightdark/modeles/Projectile  java/lang/Object VITESSE F ConstantValue?    TAILLE>L�� position Lcom/badlogic/gdx/math/Vector2; rapidite 
posInitial cadre !Lcom/badlogic/gdx/math/Rectangle; 
tempsAnime <init> A(Lcom/badlogic/gdx/math/Vector2;Lcom/badlogic/gdx/math/Vector2;)V Code
     ()V  com/badlogic/gdx/math/Vector2
  	    	    	      " com/badlogic/gdx/math/Rectangle
 ! 	  %  	  '  	 ! ) *  height	 ! , -  width LineNumberTable LocalVariableTable this %Lcom/me/lightdark/modeles/Projectile; 	direction getPosition !()Lcom/badlogic/gdx/math/Vector2; 
getInitial getCadre #()Lcom/badlogic/gdx/math/Rectangle; getRapidite setRapidite "(Lcom/badlogic/gdx/math/Vector2;)V v temps ()F update (F)V
  A B 4 cpy
  D E F scl "(F)Lcom/badlogic/gdx/math/Vector2;
  H I J add @(Lcom/badlogic/gdx/math/Vector2;)Lcom/badlogic/gdx/math/Vector2;
 ! L M N setPosition B(Lcom/badlogic/gdx/math/Vector2;)Lcom/badlogic/gdx/math/Rectangle; delta 
SourceFile Projectile.java !                	       
                                    �     W*� *� Y� � *� Y� � *� Y� � *� !Y� #� $*� &*+� *+� *,� *� $
� (*� $
� +�    .   2           %  0  5  :  ?  D  M  V  /        W 0 1     W      W 2    3 4     /     *� �    .       " /        0 1    5 4     /     *� �    .       & /        0 1    6 7     /     *� $�    .       * /        0 1    8 4     /     *� �    .       / /        0 1    9 :     >     *+� �    .   
    3  4 /        0 1      ;    < =     /     *� &�    .       8 /        0 1    > ?     j     **Y� &#b� &*� *� � @#� C� GW*� $*� � KW�    .       < 
 =  > ) @ /       * 0 1     * O    P    Q