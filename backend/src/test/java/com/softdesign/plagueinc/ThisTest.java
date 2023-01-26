package com.softdesign.plagueinc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.softdesign.plagueinc.models.traits.TraitCard;

public class ThisTest {
    
    private static TraitCard card = new TraitCard("", 0, null);

    @Test
    void testAdd(){
        Assertions.assertThat(5).isEqualTo(4 + 1);
        Assertions.assertThat(card).isEqualTo(new TraitCard("", 0, null));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            throw new IllegalArgumentException();
        });
    }
}
