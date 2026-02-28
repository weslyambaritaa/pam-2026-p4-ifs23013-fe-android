package org.delcom.pam_p4_ifs23013.data

import org.delcom.pam_p4_ifs23013.R

object DummyData {
    fun getPlantsData(): List<PlantData> {
        return listOf(
            PlantData(
                nama = "Bawang Merah",
                gambar = R.drawable.img_bawang_merah,
                deskripsi = "Bawang merah adalah umbi yang banyak digunakan sebagai bumbu dapur dan memiliki aroma kuat.",
                manfaat = "Membantu meningkatkan daya tahan tubuh, mengurangi peradangan, dan mendukung kesehatan jantung.",
                efekSamping = "Dapat menyebabkan iritasi mata dan gangguan pencernaan jika dikonsumsi berlebihan."
            ),
            PlantData(
                nama = "Bawang Putih",
                gambar = R.drawable.img_bawang_putih,
                deskripsi = "Bawang putih merupakan tanaman umbi dengan aroma khas yang sering digunakan sebagai bumbu dasar.",
                manfaat = "Mendukung kesehatan jantung, membantu meningkatkan imunitas, dan memiliki efek antibakteri alami.",
                efekSamping = "Dapat menyebabkan bau mulut, mulas, dan iritasi lambung ketika konsumsi berlebih."
            ),
            PlantData(
                nama = "Brokoli",
                gambar = R.drawable.img_brokoli,
                deskripsi = "Brokoli adalah sayuran hijau yang kaya vitamin, mineral, dan serat.",
                manfaat = "Membantu detoksifikasi, menjaga kesehatan mata, dan mendukung sistem kekebalan tubuh.",
                efekSamping = "Dapat menyebabkan kembung dan gas pada beberapa orang."
            ),
            PlantData(
                nama = "Cabai",
                gambar = R.drawable.img_cabai,
                deskripsi = "Cabai adalah buah pedas yang sering digunakan untuk menambah rasa pada makanan.",
                manfaat = "Meningkatkan metabolisme, membantu peredaran darah, dan kaya vitamin C.",
                efekSamping = "Dapat menyebabkan iritasi lambung, heartburn, dan diare jika terlalu banyak."
            ),
            PlantData(
                nama = "Kentang",
                gambar = R.drawable.img_kentang,
                deskripsi = "Kentang adalah umbi yang menjadi sumber karbohidrat dan energi.",
                manfaat = "Memberikan energi, mendukung pencernaan, dan kaya vitamin B serta kalium.",
                efekSamping = "Jika digoreng dapat meningkatkan kalori dan risiko kenaikan berat badan."
            ),
            PlantData(
                nama = "Kol",
                gambar = R.drawable.img_kol,
                deskripsi = "Kol adalah sayuran daun yang banyak digunakan dalam masakan Asia.",
                manfaat = "Membantu pencernaan, kaya vitamin C dan K, serta menjaga kesehatan kulit.",
                efekSamping = "Dapat menyebabkan gas dan perut kembung bila dikonsumsi berlebihan."
            ),
            PlantData(
                nama = "Terong",
                gambar = R.drawable.img_terong,
                deskripsi = "Terong adalah sayuran berwarna ungu atau hijau dengan tekstur lembut.",
                manfaat = "Membantu mengontrol kadar gula darah, menjaga kesehatan jantung, dan kaya antioksidan.",
                efekSamping = "Pada beberapa orang dapat menyebabkan alergi ringan atau gangguan pencernaan."
            ),
            PlantData(
                nama = "Mentimun",
                gambar = R.drawable.img_mentimun,
                deskripsi = "Mentimun adalah buah yang kaya air dan sering digunakan sebagai lalapan atau bahan salad.",
                manfaat = "Membantu hidrasi, menjaga kesehatan kulit, dan mendukung pencernaan.",
                efekSamping = "Bila terlalu banyak dapat menyebabkan sering buang air kecil atau kembung."
            ),
            PlantData(
                nama = "Tomat",
                gambar = R.drawable.img_tomat,
                deskripsi = "Tomat adalah buah yang banyak digunakan dalam masakan dan kaya antioksidan.",
                manfaat = "Mendukung kesehatan mata, menjaga kesehatan jantung, dan baik untuk kulit.",
                efekSamping = "Dapat menyebabkan asam lambung meningkat pada beberapa orang."
            ),
            PlantData(
                nama = "Wortel",
                gambar = R.drawable.img_wortel,
                deskripsi = "Wortel adalah sayuran berwarna oranye yang kaya beta-karoten.",
                manfaat = "Membantu menjaga kesehatan mata, meningkatkan imunitas, dan baik untuk kulit.",
                efekSamping = "Konsumsi berlebihan dapat membuat kulit agak kekuningan (karotenemia)."
            )
        )
    }
}