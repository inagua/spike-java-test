package ch.inagua.test.matchers;

/**
 * Created by jacques@inagua.ch on 09.02.2017.
 */
public class Existing {

    static class Error extends AssertionError {

        private static final long serialVersionUID = 1L;

        public Error(String message) {
            super("null object " + message);
        }

    }

    private Existing() {
    }

    /**
     * Voir documentation de {@link #existing(Object, String)}
     */
    public static <T> T existing(T o) {
        return existing(o, "");
    }

    /**
     * <p>
     * Cet utilitaire verifie que le parametre n'est pas null, puis le retourne. Sinon il leve une {@link Existing.Error}.
     * </p>
     *
     * Le code suivant:
     *
     * <pre>
     * assertThat(ecriture, is(not(nullValue())));
     * assertThat(ecriture.getBusinessObjectId(), equalTo(111L));
     * </pre>
     *
     * peut ainsi s'écrire de manière iso-fonctionnelle (préservation du `is/not/null`):
     *
     * <pre>
     * assertThat(existing(ecriture).getBusinessObjectId(), equalTo(111L));
     * </pre>
     *
     * ou
     *
     * <pre>
     * assertThat(existing(ecriture, &quot;ecriture&quot;).getBusinessObjectId(), equalTo(123456L));
     * </pre>
     *
     * et cela peut donc se chainer avec une bonne comprehension du message en cas d'erreur:
     *
     * <pre>
     * assertThat(existing(existing(ecriture, &quot;ecriture&quot;).getDocument(), &quot;document&quot;).getBusinessObjectId(), equalTo(222L));
     * </pre>
     *
     * Usage: voir ch.gma.contentieux.pojo.service.trans.DetailEcriturePojoDelegateTest
     *
     * @param o - objet a tester si il est null ou pas.
     * @param message - texte a afficher en cas d'erreur (ex: non de l'objet)
     * @return l'objet passe en parametre si il est non null
     * @throws Existing.Error
     */
    public static <T> T existing(T o, String message) {
        // assertThat(message, o, is(not(nullValue())));
        // assertNotNull(message, o);
        if (o == null) {
            throw new Existing.Error(message);
        }
        return o;
    }

}
