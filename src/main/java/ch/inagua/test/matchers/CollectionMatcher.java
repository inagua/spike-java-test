package ch.inagua.test.matchers;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * {@link #single(Collection)}
 */
public class CollectionMatcher {

    private CollectionMatcher() {
    }

    /**
     * <p>
     * Cet utilitaire retourne le premier element d'une collection quand elle n'en contient qu'un seul, et sinon lance une
     * AssertionError (dans tous les autres cas). Il prend donc notamment en charge le cas où la collection est null.
     * </p>
     *
     * Le code suivant:
     *
     * <pre>
     * List<String> lockIds = bo.getLockIds();
     * assertThat(lockIds, hasSize(1));
     * assertThat(lockIds.get(0), equalTo("dossier_" + bo.getBusinessObjectId()));
     * </pre>
     *
     * peut ainsi s'écrire de manière iso-fonctionnelle (préservation du `hasSize(1)`):
     *
     * <pre>
     * assertThat(single(bo.getLockIds()), equalTo("dossier_" + bo.getBusinessObjectId()));
     * </pre>
     *
     * et l'objet retourné peut etre interrogé comme un element de la collection:
     *
     * <pre>
     * final List<EcritureAllegeEntity> ecritures = getFinanceDao().loadEcrituresAllegeForGererFinance(ecritureSearchOptions);
     * assertThat(single(ecritures).getDocument().getId(), equalTo(docId));
     * </pre>
     */
    public static <T> T single(Collection<T> collection) {
        assertThat(collection, hasSize(1));
        return collection.iterator().next();
    }

}
