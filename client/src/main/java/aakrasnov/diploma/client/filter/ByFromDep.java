package aakrasnov.diploma.client.filter;

import aakrasnov.diploma.common.Filter;
import java.util.Arrays;
import java.util.Collection;

/**
 * Filter by the `from` values for the dependency for which documents were created.
 */
public class ByFromDep {
    /**
     * Target artifact id.
     */
    private final String artifact;

    /**
     * Target group id.
     */
    private final String group;

    /**
     * Target source version.
     */
    private final String vrsn;

    /**
     * Ctor.
     * @param artifact Target artifact id
     * @param group Target group id
     * @param vrsn Target source version
     */
    public ByFromDep(final String artifact, final String group, final String vrsn) {
        this.artifact = artifact;
        this.group = group;
        this.vrsn = vrsn;
    }

    /**
     * Obtains collection of filters for the information about
     * dependency which should be replaced.
     * @return Collection with filters.
     */
    public Collection<Filter> filters() {
        return Arrays.asList(
            new ArtifactId(artifact),
            new GroupId(group),
            new Version(vrsn)
        );
    }

    /**
     * Filter by artifact id.
     */
    public static final class ArtifactId extends ByArtifactId {
        /**
         * Ctor.
         *
         * @param artifact Target artifact id
         */
        public ArtifactId(final String artifact) {
            super("artifactIdFrom", artifact);
        }
    }

    /**
     * Filter by group id.
     */
    public static final class GroupId extends ByGroupId {
        /**
         * Ctor.
         *
         * @param group Target group id
         */
        public GroupId(final String group) {
            super("groupIdFrom", group);
        }
    }

    /**
     * Filter by version of artifact.
     */
    public static final class Version extends ByVersionDep {
        /**
         * Ctor.
         *
         * @param version Target version of the artifact
         */
        public Version(final String version) {
            super("versionFrom", version);
        }
    }
}
