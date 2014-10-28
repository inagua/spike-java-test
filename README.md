# Eloquent JUnit: from Matchers to Builders

* Sources
* Contexte
* The test: verbose
* The matcher: consise
* The Builder: eloquent
* Améliorations
* Conclusion

 
## Sources

Ce projet maven contient les sources :

	matchers
	 + src/main/java
	    + ch.inagua.spikes.matchers
	       + models
	          - Colleague                  // Class to test
	       + services
	          - ColleagueBuilder           // Design Pattern Builder
	          - Recruiter                  // Uses Builder
	 + src/test/java
	    + ch.inagua.spikes.matchers
	       + matchers
	          - IsColleagueBuilderMatcher  // Matcher with Builder   
	          - IsColleagueMatcher         // Matcher v0
	       + services
	          - RecruiterTest              // Test using the Matcher
	 + pom.xml                             // Contains needed dependencies
	 + README.md

Vous pouvez l'ouvrir avec votre IDE Java préféré.

Un `mvn test` vous montrera le test en échec.

## Context

Je devais faire une évolution dans une portion de code _legacy_, qui manipulait une grappe d'objets. En fait, le code générait des instances de la classe `Demande` (plusieurs milliers) à partir d'un `ResultSet`.

La particularité de la grappe objet était que, la `Demande` avait :

* de nombreux attributs, comme des dates ou des montants, 
* mais aussi d'autres objets: depuis la racine `Demande` il y avait 9 niveaux, et certain de ses enfants avaient plus de 20 attributs.

J'ai donc commencé par écrire des tests de non régression.

Note : afin de ne pas avoir de soucis de confidentialité par rapport à mon projet, et de pouvoir fournir un code autonome qui ne tire pas de dépendances, je vais remplacer la `Demande` par une simple classe `Colleague` qui possède quelques attributs.  

## The test: verbose

J'ai commencé basiquement par écrire les tests unitaires avec des `assertEquals`.
Le test me prenait alors une ligne par attribut à tester, autant dire de nombreuses lignes dans mon cas :

	assertEquals(colleague.getName(), "Jacques");
	assertNull(colleague.getService());
	assertEquals(colleague.getAge(), 0);
	assertNull(colleague.getCurrentProject());
	assertEquals(colleague.getSalary().longValue(), 100000L);


Clair mais répétitif et verbeux....

## The matcher: consise

J'ai alors créé un `Matcher` qui me permet d'écrire le test en une ligne :

	assertThat(colleague, is(IsColleagueMatcher.colleagueWith("Jacques", 0, null, null, "100000")));

Qui avec un import static s'écrit ainsi:

	import static ch.inagua.spikes.matchers.matcher.IsColleagueMatcher.colleagueWith;
	// ...
	assertThat(colleague, is(colleagueWith("Jacques", 0, null, null, "100000")));


Le `Matcher` est le _Design Pattern_ implémenté par `hamcrest` pour écrire ses propres tests comme `assertThat(myCat.hasJump(), is(true));` ou
`is(...)` est un `Matcher`.

Le code du `Matcher` est le suivant :

	public class IsColleagueMatcher extends TypeSafeMatcher<Colleague> {
	
		private final String name;
		private final int age;
		private final String service;
		private final String currentProject;
		private final BigDecimal salary;
	
		/**
		 * Constructor, private!... @see {@link #colleagueWith(String, int, String, String, String)}
		 */
		private IsColleagueMatcher(String name, int age, String service, String currentProject, String salary) {
			this.name = name;
			this.age = age;
			this.service = service;
			this.currentProject = currentProject;
			this.salary = new BigDecimal(salary);
		}
	
		/**
		 * Static method to return an instance of the matcher
		 */
		@Factory
		public static IsColleagueMatcher colleagueWith(String name, int age, String service, String currentProject, String salary) {
			return new IsColleagueMatcher(name, age, service, currentProject, new BigDecimal(salary));
		}
	
		/**
		 * toString method for the Expected (values given to the factory above)
		 */
		public void describeTo(Description description) {
			description.appendText("colleague with properties [" //
					+ "name=" + name //
					+ ", age=" + age //
					+ ", service=" + service //
					+ ", currentProject=" + currentProject //
					+ ", salary=" + salary //
					+ "]");
		}
	
		/**
		 * toString method for the Actual / tested instance of the object
		 */
		@Override
		protected void describeMismatchSafely(Colleague colleague, Description description) {
			description.appendText("was [" //
					+ (StringUtils.equals(name, colleague.getName()) ? "" : "name=" + colleague.getName()) //
					+ (age == colleague.getAge() ? "" : ", age=" + colleague.getAge()) //
					+ (StringUtils.equals(service, colleague.getService()) ? "" : ", service=" + colleague.getService()) //
					+ (StringUtils.equals(currentProject, colleague.getCurrentProject()) ? "" : ", currentProject=" + colleague.getCurrentProject()) //
					+ (areBigDecimalEquals(salary, colleague.getSalary()) ? "" : ", salary=" + colleague.getSalary()) //
					+ "]");
		}
	
		/**
		 * Do the comparison
		 */
		@Override
		protected boolean matchesSafely(Colleague colleague) {
			return true //
					&& StringUtils.equals(name, colleague.getName())//
					&& age == colleague.getAge()//
					&& StringUtils.equals(service, colleague.getService())//
					&& StringUtils.equals(currentProject, colleague.getCurrentProject())//
					&& areBigDecimalEquals(salary, colleague.getSalary())//
			;
		}
	
		/**
		 * Private stuff
		 */
		private boolean areBigDecimalEquals(BigDecimal bd1, BigDecimal bd2) {
			if (bd1 == null && bd2 == null) return true;
			if (bd1 != null) return bd1.equals(bd2);
			return bd2.equals(bd1);
		}
	
	}

Bien qu'on ait gagné en concision, cette écriture pose deux problèmes :

* A quoi correspond chaque paramètre (par exemple le 3è paramètre `null`) ?
* Il fait spécifier les deux paramètres avec une valeur `null`


### Technique

Pour écrire un `Matcher`, la classe de base étant `org.hamcrest.TypeSafeMatcher`, il faut la librairie `hamcrest` :

		<dependency>
			<groupId>org.hamcrest</groupId>
			<artifactId>hamcrest-all</artifactId>
			<scope>test</scope>
			<version>1.3</version>
		</dependency>


## The Builder: eloquent

C'est pour addresser le premier problème que j'ai mis en oeuvre le _Design Pattern_ `Builder`, ou une adaptation pour être plus exact.

Le _Design Pattern_ `Builder` permet de créer une instance d'un objet (le `Builder`) qui permet de créer l'objet que l'on souhaite, puis en chainant des appels sur cet objet on peut renseigner ses attributs. A la fin, un appel à une méthode `build()` permet d'insctancier l'objet souhaité

La vocation du `Builder` est bien de pouvoir construire des instances en chainant les appels, sur une ligne.

Voici comme exemple le `Builder` de notre classe `Colleague` :

	public class ColleagueBuilder {
	
		private ColleagueBuilder() {
		}
	
		public static ColleagueBuilder builder() {
			return new ColleagueBuilder();
		}
	
		public Colleague build() {
			final Colleague colleague = new Colleague();
			colleague.setName(name);
			colleague.setAge(age);
			colleague.setService(service);
			colleague.setCurrentProject(currentProject);
			colleague.setSalary(salary);
			return colleague;
		}
	
		private String name;
		private int age;
		private String service;
		private String currentProject;
		private BigDecimal salary;
	
		public ColleagueBuilder name(String name) {
			this.name = name;
			return this;
		}
	
		public ColleagueBuilder service(String service) {
			this.service = service;
			return this;
		}
	
		public ColleagueBuilder age(int age) {
			this.age = age;
			return this;
		}
	
		public ColleagueBuilder currentProject(String currentProject) {
			this.currentProject = currentProject;
			return this;
		}
	
		public ColleagueBuilder salary(BigDecimal salary) {
			this.salary = salary;
			return this;
		}
	
	}

Une fois ce Builder défini, on crée une Person ainsi:

	Colleague c = ColleagueBuilder.builder().name("Batman").age(33).build();

Ce qui est d'une redoutable et séduisante concision.

Pour information il existe des librairies qui génèrent les Builder.

Pour revenir à notre sujet, on va réutiliser ce _pattern_ ainsi :

* Le `Builder` sera le `Matcher`
* Pas besoin de la partie `build()`

Cela donne le code suivant :

	public class IsColleagueBuilderMatcher extends TypeSafeMatcher<Colleague> {
	
		//
		// MACTHER PART
		//
	
		/**
		 * Constructor, private!... @see {@link #colleagueWith()}
		 */
		private IsColleagueBuilderMatcher() {
		}
	
		/**
		 * Static method to return an instance of the matcher
		 */
		@Factory
		public static IsColleagueBuilderMatcher colleagueWith() {
			return new IsColleagueBuilderMatcher();
		}
	
		/**
		 * toString method for the Expected (values given to the factory above)
		 */
		public void describeTo(Description description) {
			description.appendText("colleague with properties [" //
					+ "name=" + name //
					+ ", age=" + age //
					+ ", service=" + service //
					+ ", currentProject=" + currentProject //
					+ ", salary=" + salary //
					+ "]");
		}
	
		/**
		 * toString method for the Actual / tested instance of the object
		 */
		@Override
		protected void describeMismatchSafely(Colleague colleague, Description description) {
			description.appendText("was [" //
					+ (StringUtils.equals(name, colleague.getName()) ? "" : "name=" + colleague.getName()) //
					+ (age == colleague.getAge() ? "" : ", age=" + colleague.getAge()) //
					+ (StringUtils.equals(service, colleague.getService()) ? "" : ", service=" + colleague.getService()) //
					+ (StringUtils.equals(currentProject, colleague.getCurrentProject()) ? "" : ", currentProject=" + colleague.getCurrentProject()) //
					+ (areBigDecimalEquals(salary, colleague.getSalary()) ? "" : ", salary=" + colleague.getSalary()) //
					+ "]");
		}
	
		/**
		 * Do the comparison!
		 */
		@Override
		protected boolean matchesSafely(Colleague colleague) {
			return true //
					&& StringUtils.equals(name, colleague.getName())//
					&& age == colleague.getAge()//
					&& StringUtils.equals(service, colleague.getService())//
					&& StringUtils.equals(currentProject, colleague.getCurrentProject())//
					&& areBigDecimalEquals(salary, colleague.getSalary())//
			;
		}
	
		/**
		 * Private stuff
		 */
		private boolean areBigDecimalEquals(BigDecimal bd1, BigDecimal bd2) {
			if (bd1 == null && bd2 == null) return true;
			if (bd1 != null) return bd1.equals(bd2);
			return bd2.equals(bd1);
		}
	
		//
		// BUILDER part
		//
	
		private String name;
		private int age;
		private String service;
		private String currentProject;
		private BigDecimal salary;
	
		/**
		 * Setter for name
		 */
		public IsColleagueBuilderMatcher name(String name) {
			this.name = name;
			return this;
		}
	
		/**
		 * Setter for age
		 */
		public IsColleagueBuilderMatcher age(int age) {
			this.age = age;
			return this;
		}
	
		/**
		 * Setter for service
		 */
		public IsColleagueBuilderMatcher service(String service) {
			this.service = service;
			return this;
		}
	
		/**
		 * Setter for currentProject
		 */
		public IsColleagueBuilderMatcher currentProject(String currentProject) {
			this.currentProject = currentProject;
			return this;
		}
	
		/**
		 * Setter for salary
		 */
		public IsColleagueBuilderMatcher salary(String salary) {
			this.salary = new BigDecimal(salary);
			return this;
		}
	
	}

Avec l'appel suivant à l'utilisation :

    assertThat(colleague, is(colleagueWith().name("Batman").age(33)));

L'autre intérêt est que, avec cette implémentation, il n'est plus besoin d'écrire explicitement les méthodes qui prennent un `null` en paramètre, puisque c'est la valeur par défaut. Ceci étant la seconde limite en conclusion du paragraphe suivant... CQFD!

Ainsi, un test qui passe écrit ainsi :

    assertThat(colleague, is(colleagueWith().name("Batman").age(33).service(null)));

Peut aussi s'écrire ainsi avec succès :

    assertThat(colleague, is(colleagueWith().name("Batman").age(33)));

## Améliorations

Cette partie est un parti prix, qui n'engage que moi.

### Qui dit DSL, dit facile à lire

Pour ma part, je considère le test comme une documentation, et j'attache beaucoup d'importance à ce qu'ils soient lisible... Sans "code admninistratif".
Ce que j'appelle du "code admninistratif" c'est le code technique, qui n'apporte rien à la compréhension.

Un bon exemple ici est le salaire, de type `BigDecimal`.

En jetant un oeil attentif aux différentes versions de `Matcher` ci dessus, vous constaterez que le `salary` est de type `String` et que le `BigDecimal` est construit en interne.

Au lieu d'avoir :

    assertThat(colleague, is(colleagueWith().salary(new BigDecimal("100000"))));

nous obtenons :  

    assertThat(colleague, is(colleagueWith().salary("100000")));

Le `Matcher` joue le rôle de _proxy_ qui permet d'enfouir ce "code admninistratif"

### Nommage

Etant donné que les `Matchers` comportent bon nombre de méthodes, en plus de celles qui permettent de spécifier les attributs, je préfixe ces dernières par un underscore "_" afin de les regrouper lors de la complétion.

Le code suivant :

		public IsDemandeWithProperties codeDocument(String codeDocument) {
			this.codeDocument = codeDocument;
			return this;
		}
		// ...
	    assertThat(demande, is(demandeWith().codeDocument("ABC")));

Devient ainsi :

		public IsDemandeWithProperties _codeDocument(String codeDocument) {
			this.codeDocument = codeDocument;
			return this;
		}
		// ...
	    assertThat(demande, is(demandeWith()._codeDocument("ABC")));

### N'afficher que les champs en échec

Par défaut, en cas d'échèc, l'intégralité des attributs des _beans_ _Expected_ et _Actual_ sont affichés... Ce qui ne rend difficile l'identification de l'attribut qui provoque l'échec dans le cas où les attributs sont nombreux.

Pour cela, j'ai amélioré la méthode `describeMismatchSafely(Colleague, Description)`, chargée d'affiché le _Actual_ en cas d'échec.

	@Override
	protected void describeMismatchSafely(Colleague colleague, Description description) {
		description.appendText("was [" //
				+ "name=" + colleague.getName() //
				+ ", age=" + colleague.getAge() //
				+ ", service=" + colleague.getService() //
				+ ", currentProject=" + colleague.getCurrentProject() //
				+ ", salary=" + colleague.getSalary() //
				+ "]");
	}

Qui genère le message d'erreur :

	Expected: is colleague with properties [name=Batman, age=0, service=null, currentProject=null, salary=100000]
		but: was [name=Jacques, age=0, service=null, currentProject=null, salary=100000]

devient ainsi :

	@Override
	protected void describeMismatchSafely(Colleague colleague, Description description) {
		description.appendText("was [" //
				+ (StringUtils.equals(name, colleague.getName()) ? "" : "name=" + colleague.getName()) //
				+ (age == colleague.getAge() ? "" : ", age=" + colleague.getAge()) //
				+ (StringUtils.equals(service, colleague.getService()) ? "" : ", service=" + colleague.getService()) //
				+ (StringUtils.equals(currentProject, colleague.getCurrentProject()) ? "" : ", currentProject=" + colleague.getCurrentProject()) //
				+ (areBigDecimalEquals(salary, colleague.getSalary()) ? "" : ", salary=" + colleague.getSalary()) //
				+ "]");
	}

avec le message d'erreur :

	Expected: is colleague with properties [name=Batman, age=0, service=null, currentProject=null, salary=100000]
		but: was [name=Jacques]


## Conclusion

La mise en place de ce pattern `Builder Matcher` permet l'emrgence d'un élégant DSL (_Domain Specific Language_), rend les tests plus lisibles, et donc plus facilement maintenables.

