package aakrasnov.diploma.common.cache;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.RsBaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocValidityCheckRsDto extends RsBaseDto {
    /**
     * This field may be equal to {@code null}.
     * It is possible when document does not exist in database or
     * when locally cached document is valid.
     */
    private DocDto docDto;

    private ServerAnswer serverAnswer;

    public static enum ServerAnswer {
        /**
         * Document exists in the db and only id matches.
         * It is necessary to read such document from server
         * and refresh local cache.
         */
        EXIST_ONLY_BY_ID,

        /**
         * Document with such id not found in db.
         * It is necessary to print warning that document exists only in the local cache.
         */
        NOT_EXIST,

        /**
         * Locally cached document is valid.
         * It is necessary to read document from cache as it is valid.
         */
        EXIST_BY_ID_AND_TIMESTAMP;
    }
}
