-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 10-Jun-2018 às 20:31
-- Versão do servidor: 10.1.31-MariaDB
-- PHP Version: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smartedu`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `user`
--

CREATE TABLE `user` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `position_id` int(10) UNSIGNED NOT NULL,
  `campus_id` int(10) UNSIGNED DEFAULT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Extraindo dados da tabela `user`
--

INSERT INTO `user` (`id`, `name`, `email`, `password`, `status`, `position_id`, `campus_id`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'Johnny Rockembach', 'johnny@alunos.utfpr.edu.br', '$2y$10$eB71o10PoDQrERVcleU05OXGNEV/NEE041uq1xj0w1omLOTgONCX6', 1, 1, NULL, NULL, '2018-04-13 02:33:33', '2018-04-13 02:33:33'),
(2, 'Desenvolvedor 1', 'desenvolvedor@smartedu.com.br', '$2y$10$iVQTiHt5fG8qgx/0WFGhG.1gD7fRU2PDwnGAhLQgvz3yG7znoqURi', 1, 1, NULL, NULL, '2018-04-13 02:33:33', '2018-04-13 02:33:33'),
(3, 'Administrador Instituição', 'administrador@instituicao.com.br', '$2y$10$NUK86UhrNqTbFlduEUjYVuE0lEmTY1Cu.Mq4IZz1itqT552wF6s4W', 1, 2, NULL, NULL, '2018-06-10 21:30:03', '2018-06-10 21:30:03'),
(4, 'Administrador Pato Branco', 'administrador@patobranco.com.br', '$2y$10$g5EMQqjqhipVVrC/NaCVLOd5ft7v/3LzD5Nq2TxZVE7WaQn/FBw.C', 1, 3, 1, NULL, '2018-06-10 21:30:03', '2018-06-10 21:30:03'),
(5, 'Administrador Curitiba', 'administrador@curitiba.com.br', '$2y$10$E025nZsq9NuAqN/.jZ50aO1w8hC0kYnUxT5utKZpc9E3myTJZlB0O', 1, 3, 3, NULL, '2018-06-10 21:30:03', '2018-06-10 21:30:03'),
(6, 'Coordenador Pato Branco', 'coordenador@patobranco.com.br', '$2y$10$tPiLPxeAhVQtHWNHgl81MeEXGV6RUuEAIaIOopSiyBwxl.OToOyyC', 1, 4, 1, NULL, '2018-06-10 21:30:04', '2018-06-10 21:30:04'),
(7, 'Coordenador Curitiba', 'coordenador@curitiba.com.br', '$2y$10$6UWd8YnU1rGJs0EmHVhJP.aioCrR5QuLs27OsZZPzi4m65Hvwc.wa', 1, 4, 3, NULL, '2018-06-10 21:30:04', '2018-06-10 21:30:04'),
(8, 'Professor Pato Branco', 'professor@patobranco.com.br', '$2y$10$K7GkGUnhyrtbnuRFtcjpH.qaoIGVZQNdTZslpeis4Je.RWvXICMwO', 1, 5, 1, NULL, '2018-06-10 21:30:04', '2018-06-10 21:30:04'),
(9, 'Professor Curitiba', 'professor@curitiba.com.br', '$2y$10$vXobrJUDuYkY9c/mjSQDk.Z.Jsl/V1IPM7YqJju4EBycLNLK7panK', 1, 5, 3, NULL, '2018-06-10 21:30:04', '2018-06-10 21:30:04');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_email_unique` (`email`),
  ADD KEY `user_position_id_foreign` (`position_id`),
  ADD KEY `user_campus_id_foreign` (`campus_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_campus_id_foreign` FOREIGN KEY (`campus_id`) REFERENCES `campus` (`id`),
  ADD CONSTRAINT `user_position_id_foreign` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
